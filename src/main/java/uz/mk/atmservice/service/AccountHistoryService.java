package uz.mk.atmservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.mk.atmservice.repository.AccountHistoryRepository;

@Service
public class AccountHistoryService {
    @Autowired
    AccountHistoryRepository accountHistoryRepository;



}
