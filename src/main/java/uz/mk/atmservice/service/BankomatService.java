package uz.mk.atmservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.mk.atmservice.entity.*;
import uz.mk.atmservice.entity.enums.RoleName;
import uz.mk.atmservice.payload.ApiResponse;
import uz.mk.atmservice.payload.BankomatDto;
import uz.mk.atmservice.payload.ClientMoneyDto;
import uz.mk.atmservice.payload.MoneyDto;
import uz.mk.atmservice.repository.*;
import uz.mk.atmservice.utils.CommonUtils;

import java.util.*;
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

    //ADD NEW BANKOMAT TO DB
    public ApiResponse add(BankomatDto bankomatDto) {
        Map<String, Object> securityContextHolder = CommonUtils.getPrincipalAndRoleFromSecurityContextHolder();
        Set<Role> principalUserRoles = (Set<Role>) securityContextHolder.get("principalUserRoles");
        boolean existsStaffAuthority = CommonUtils.isExistsAuthority(principalUserRoles, RoleName.ROLE_STAFF);

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
        boolean existsStaffAuthority = CommonUtils.isExistsAuthority(principalUserRoles, RoleName.ROLE_STAFF);

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
    public ApiResponse withdraw(ClientMoneyDto clientMoneyDto, UUID cardId) {
        Card card = cardRepository.findById(cardId).get();

        Double summa = clientMoneyDto.getSumma();
        double newSumma = card.getBalance() - summa;
        if (newSumma < 0) {
            return new ApiResponse("There is not enough money from card to withdraw", false);
        }

        List<Banknote> banknotes = banknoteRepository.findAll();
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

        Bankomat bankomat = bankomatRepository.findById(clientMoneyDto.getBankomatId()).get();
        bankomat.setBalance(bankomat.getBalance() - summa);
        bankomatRepository.save(bankomat);
        AccountType accountType = accountTypeRepository.findById(clientMoneyDto.getAccountTypeId()).get();


        Integer value = banknote.getValue();
        double amount = summa / value;

        AccountHistory accountHistory = CommonUtils.createAccountHistory(banknote, (int) amount, card, accountType, bankomat);
        AccountHistory savedAccountHistory = accountHistoryRepository.save(accountHistory);

        card.setBalance(newSumma);
        cardRepository.save(card);

        return new ApiResponse("Money was withdrawn from the card", true, savedAccountHistory);
    }


}
