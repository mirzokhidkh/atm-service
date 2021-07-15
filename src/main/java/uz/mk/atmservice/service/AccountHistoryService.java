package uz.mk.atmservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.mk.atmservice.entity.AccountHistory;
import uz.mk.atmservice.entity.AccountType;
import uz.mk.atmservice.entity.Role;
import uz.mk.atmservice.entity.enums.AccountTypeName;
import uz.mk.atmservice.entity.enums.RoleName;
import uz.mk.atmservice.payload.ApiResponse;
import uz.mk.atmservice.repository.AccountHistoryRepository;
import uz.mk.atmservice.repository.AccountTypeRepository;
import uz.mk.atmservice.utils.CommonUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static uz.mk.atmservice.utils.CommonUtils.*;

@Service
public class AccountHistoryService {
    @Autowired
    AccountHistoryRepository accountHistoryRepository;

    @Autowired
    AccountTypeRepository accountTypeRepository;

    public ApiResponse getAllAccountHistory(Integer bankomatId) {
        Map<String, Object> securityContextHolder = CommonUtils.getPrincipalAndRoleFromSecurityContextHolder();
        Set<Role> principalUserRoles = (Set<Role>) securityContextHolder.get("principalUserRoles");
        boolean existsDirectorAuthority = CommonUtils.isExistsAuthority(principalUserRoles, RoleName.ROLE_DIRECTOR);

        if (!existsDirectorAuthority){
            return new ApiResponse("You don't have the authority", false);
        }

        List<AccountHistory> accountHistories = accountHistoryRepository.findAllByBankomatId(bankomatId);
        return new ApiResponse("Account Histories", true, accountHistories);
    }

    public ApiResponse getDailyIncomesOrExpenses(Integer bankomatId, String dateText,AccountTypeName accountTypeName) {
        Map<String, Object> securityContextHolder = CommonUtils.getPrincipalAndRoleFromSecurityContextHolder();
        Set<Role> principalUserRoles = (Set<Role>) securityContextHolder.get("principalUserRoles");
        boolean existsDirectorAuthority = CommonUtils.isExistsAuthority(principalUserRoles, RoleName.ROLE_DIRECTOR);

        if (!existsDirectorAuthority){
            return new ApiResponse("You don't have the authority", false);
        }

        Date date;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(dateText);
        } catch (Exception dateException) {
            return new ApiResponse("Date incorrect", false, dateException);
        }

        Timestamp minDate, maxDate;

        Calendar calendar1 = getCalendarForDate(date);
        setTimeToBeginningOfDay(calendar1);
        minDate = new Timestamp(calendar1.getTime().getTime());

        Calendar calendar2 = getCalendarForDate(date);
        setTimeToEndOfDay(calendar2);
        maxDate = new Timestamp(calendar2.getTime().getTime());

        AccountType accountType = accountTypeRepository.findByName(accountTypeName);

        List<AccountHistory> accountHistories = accountHistoryRepository
                .findAllByBankomatIdAndAccountTypeIdAndDateBetween(bankomatId, accountType.getId(), minDate, maxDate);
        return new ApiResponse("Account Histories", true, accountHistories);
    }







}
