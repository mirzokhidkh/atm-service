package uz.mk.atmservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.mk.atmservice.entity.*;
import uz.mk.atmservice.entity.enums.RoleName;
import uz.mk.atmservice.payload.ApiResponse;
import uz.mk.atmservice.payload.BankomatDto;
import uz.mk.atmservice.payload.ClientMoneyDto;
import uz.mk.atmservice.payload.MoneyDto;
import uz.mk.atmservice.repository.*;
import uz.mk.atmservice.utils.CommonUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class BankomatService {
    @Autowired
    BankomatRepository bankomatRepository;

    @Autowired
    CommissionSetRepository commissionSetRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    BankRepository bankRepository;

    @Autowired
    CardTypeRepository cardTypeRepository;

    @Autowired
    BanknoteRepository banknoteRepository;

    @Autowired
    ReplenishAtmHistoryRepository replenishAtmHistoryRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    AccountHistoryRepository accountHistoryRepository;

    @Autowired
    AccountTypeRepository accountTypeRepository;

    @Autowired
    MailService mailService;

    //ADD NEW BANKOMAT TO DB
    public ApiResponse add(BankomatDto bankomatDto) {
        Map<String, Object> securityContextHolder = CommonUtils.getPrincipalAndRoleFromSecurityContextHolder();
        Set<Role> principalUserRoles = (Set<Role>) securityContextHolder.get("principalUserRoles");
        boolean existsStaffAuthority = CommonUtils.isExistsAuthority(principalUserRoles, RoleName.ROLE_ATM_STAFF);

        if (!existsStaffAuthority) {
            return new ApiResponse("You don't have the authority", false);
        }

        Bankomat bankomat = new Bankomat();
        bankomat.setMaxMoney(bankomatDto.getMaxMoney());
        bankomat.setMinMoney(bankomatDto.getMinMoney());
        bankomat.setCommissionSet(commissionSetRepository.findById(bankomatDto.getCommissionSetId()).get());
        Address address = new Address(
                bankomatDto.getPlace(),
                bankomatDto.getStreet(),
                bankomatDto.getDistrict()
        );
        Address savedAddress = addressRepository.save(address);
        bankomat.setAddress(savedAddress);

        bankomat.setBank(bankRepository.findById(bankomatDto.getBankId()).get());

        List<CardType> cardTypes = cardTypeRepository.findAllByIdIn(bankomatDto.getCardTypeIds());
        bankomat.setCardTypes(cardTypes);
        List<Banknote> banknotes = banknoteRepository.findAllByIdIn(bankomatDto.getBanknoteIds());
        bankomat.setBanknotes(banknotes);
        Bankomat savedBankomat = bankomatRepository.save(bankomat);

        return new ApiResponse("Bankomat saved", true, savedBankomat);
    }


    //REPLENISH MONEY TO BALANCE OF BANKOMAT
    public ApiResponse fill(MoneyDto moneyDto) {
        Map<String, Object> securityContextHolder = CommonUtils.getPrincipalAndRoleFromSecurityContextHolder();
        User principalUser = (User) securityContextHolder.get("principalUser");
        Set<Role> principalUserRoles = (Set<Role>) securityContextHolder.get("principalUserRoles");
        boolean existsStaffAuthority = CommonUtils.isExistsAuthority(principalUserRoles, RoleName.ROLE_ATM_STAFF);

        if (!existsStaffAuthority) {
            return new ApiResponse("You don't have the authority", false);
        }

        Integer amount = moneyDto.getAmount();
        Banknote banknote = banknoteRepository.findById(moneyDto.getBanknoteId()).get();
        Bankomat bankomat = bankomatRepository.findById(moneyDto.getBankomatId()).get();

        ReplenishAtmHistory replenishAtmHistory = new ReplenishAtmHistory();
        replenishAtmHistory.setBanknote(banknote);
        replenishAtmHistory.setAmount(amount);
        replenishAtmHistory.setBankomat(bankomat);
        replenishAtmHistory.setStaff(principalUser);
        ReplenishAtmHistory savedHistory = replenishAtmHistoryRepository.save(replenishAtmHistory);

        List<Banknote> oldBanknotes = bankomat.getBanknotes();
        boolean anyMatch = oldBanknotes.stream().anyMatch(banknote1 -> banknote1.equals(banknote));

        if (!anyMatch) {
            oldBanknotes.add(banknote);
            bankomat.setBanknotes(oldBanknotes);
        }

        bankomat.setBalance(bankomat.getBalance() + savedHistory.getSumma());
        bankomatRepository.save(bankomat);

        return new ApiResponse("Replenishing ATM history saved", true, savedHistory);
    }


    //WITHDRAW MONEY FROM BANKOMAT
    @Transactional
    public ApiResponse withdraw(ClientMoneyDto clientMoneyDto) {
        Card card =(Card) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Bankomat bankomat = bankomatRepository.findById(clientMoneyDto.getBankomatId()).get();
        Double commissionToWithdraw = bankomat.getCommissionSet().getCommissionToWithdraw();

        Double summa = clientMoneyDto.getSumma();
        double summaWithCommission = summa * (1 + commissionToWithdraw / 100);
        double newSumma = card.getBalance() - summaWithCommission;
        if (newSumma < 0) {
            return new ApiResponse("There is not enough money from card to withdraw", false);
        }

        List<Banknote> banknotes = bankomat.getBanknotes();
        List<Banknote> banknoteList = banknotes.stream()
                .filter(banknote ->
                        summa % banknote.getValue() == 0 && banknote.getCurrency().getId().equals(card.getCurrency().getId()))
                .collect(Collectors.toList());
        Banknote banknote = null;
        if (banknoteList.size() > 0) {
            banknote = banknoteList.get(banknoteList.size() - 1);
        } else {
            return new ApiResponse("Please enter other summa", false);
        }
        card.setBalance(newSumma);
        bankomat.setBalance(bankomat.getBalance() - summa);
        if (bankomat.getBalance() - bankomat.getMinMoney() <= 0) {
            List<User> bankomatStaff = bankomat.getBank().getStaff().stream().filter(user -> {
                List<Role> roles = user.getRoles()
                        .stream().filter(role -> role.getName().equals(RoleName.ROLE_ATM_STAFF)).collect(Collectors.toList());
                return roles.size() != 0;
            }).collect(Collectors.toList());
            String text = "Balance at the ATM was less than the minimum amount. Don't remember to fill balance !";
            mailService.sendEmail(bankomatStaff.get(0).getEmail(), "WARNING", text);
        }

        AccountType accountType = accountTypeRepository.findById(clientMoneyDto.getAccountTypeId()).get();


        Integer value = banknote.getValue();
        double amount = summa / value;

        AccountHistory accountHistory = CommonUtils.createAccountHistory(banknote, (int) amount, card,
                accountType, bankomat, commissionToWithdraw);
        AccountHistory savedAccountHistory = accountHistoryRepository.save(accountHistory);

        cardRepository.save(card);

        return new ApiResponse("Money was withdrawn from the card", true, savedAccountHistory);
    }


    //REPLENISH MONEY CARD
    @Transactional
    public ApiResponse replenishCard(MoneyDto moneyDto) {
        Card card =(Card) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Bankomat bankomat = bankomatRepository.findById(moneyDto.getBankomatId()).get();
        Banknote banknote = banknoteRepository.getById(moneyDto.getBanknoteId());
        List<Banknote> banknotes = bankomat.getBanknotes();
        boolean anyMatch = banknotes.stream().anyMatch(banknote1 -> banknote1.getId().equals(banknote.getId()));
        if (!anyMatch) {
            return new ApiResponse("Money with such a banknote doesn't exists now", false);
        }
        Double commissionToReplenish = bankomat.getCommissionSet().getCommissionToReplenish();
        Integer amount = moneyDto.getAmount();
        int summaWithCommission = banknote.getValue() * amount;
        double summaWithoutCommission = (summaWithCommission * 100) / (100 + commissionToReplenish);
        double newSumma = card.getBalance() + summaWithoutCommission;
        card.setBalance(newSumma);
        bankomat.setBalance(bankomat.getBalance() + summaWithCommission);

        boolean anyMatch1 = banknotes.stream()
                .anyMatch(aBanknote ->
                        summaWithCommission % aBanknote.getValue() == 0 && aBanknote.getCurrency().getId().equals(card.getCurrency().getId()));

        if (!anyMatch1) {
            return new ApiResponse("Please enter other summa", false);
        }

        AccountType accountType = accountTypeRepository.findById(moneyDto.getAccountTypeId()).get();

        AccountHistory accountHistory = CommonUtils.createAccountHistory(banknote, amount, card,
                accountType, bankomat, commissionToReplenish);
        AccountHistory savedAccountHistory = accountHistoryRepository.save(accountHistory);
        bankomatRepository.save(bankomat);
        cardRepository.save(card);

        return new ApiResponse("The money fell on the card", true, savedAccountHistory);
    }


}
