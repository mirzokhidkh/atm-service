package uz.mk.atmservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.mk.atmservice.entity.Card;
import uz.mk.atmservice.payload.ApiResponse;
import uz.mk.atmservice.payload.CardDto;
import uz.mk.atmservice.repository.*;

@Service
public class CardService {


    @Autowired
    CardRepository cardRepository;

    @Autowired
    CardTypeRepository cardTypeRepository;

    @Autowired
    BankRepository bankRepository;

    @Autowired
    RoleRepository roleRepository;

    public ApiResponse addCard(CardDto cardDto) {
        boolean existsByNumber = cardRepository.existsByNumber(cardDto.getNumber());
        if (existsByNumber) {
            return new ApiResponse("card with such a number already exists", false);
        }

        Card card = new Card();
        card.setNumber(cardDto.getNumber());
        card.setCvvCode(cardDto.getCvvCode());
        card.setFullName(cardDto.getFullName());
        card.setValidityPeriod(cardDto.getValidityPeriod());
        card.setCode(cardDto.getCode());
        card.setCardType(cardTypeRepository.findById(cardDto.getCardTypeId()).get());
        card.setBank(bankRepository.findById(cardDto.getBankId()).get());
        card.setStatus(cardDto.isStatus());
        card.setRole(roleRepository.findById(cardDto.getRoleId()).get());

        Card savedCard = cardRepository.save(card);

        return new ApiResponse("Card saved", true, savedCard);

    }
}
