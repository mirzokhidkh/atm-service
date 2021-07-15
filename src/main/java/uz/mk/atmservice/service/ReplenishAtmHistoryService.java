package uz.mk.atmservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.mk.atmservice.entity.ReplenishAtmHistory;
import uz.mk.atmservice.entity.Role;
import uz.mk.atmservice.entity.enums.RoleName;
import uz.mk.atmservice.payload.ApiResponse;
import uz.mk.atmservice.repository.ReplenishAtmHistoryRepository;
import uz.mk.atmservice.utils.CommonUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static uz.mk.atmservice.utils.CommonUtils.checkAuthority;

@Service
public class ReplenishAtmHistoryService {
    @Autowired
    ReplenishAtmHistoryRepository replenishAtmHistoryRepository;

    public ApiResponse getReplenishingBankomatHistories(Integer bId) {
        if (checkAuthority(RoleName.ROLE_DIRECTOR)) {
            return new ApiResponse("You don't have the authority", false);
        }

        List<ReplenishAtmHistory> replenishAtmHistoryList = replenishAtmHistoryRepository.findAllByBankomatId(bId);
        return new ApiResponse("Replenishing ATM histories", true, replenishAtmHistoryList);
    }


}
