package uz.mk.atmservice.service;

import uz.mk.atmservice.payload.ApiResponse;
import uz.mk.atmservice.payload.BankomatDto;
import uz.mk.atmservice.payload.ClientMoneyDto;
import uz.mk.atmservice.payload.MoneyDto;

public interface BankomatService {

    ApiResponse add(BankomatDto bankomatDto);

    ApiResponse fill(MoneyDto moneyDto);

    ApiResponse withdraw(ClientMoneyDto clientMoneyDto);

    ApiResponse replenishCard(MoneyDto moneyDto);
}
