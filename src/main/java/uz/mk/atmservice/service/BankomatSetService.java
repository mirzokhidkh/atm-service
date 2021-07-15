package uz.mk.atmservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.mk.atmservice.entity.BankomatSet;
import uz.mk.atmservice.entity.enums.RoleName;
import uz.mk.atmservice.payload.ApiResponse;
import uz.mk.atmservice.repository.BankomatSetRepository;

import java.util.List;

import static uz.mk.atmservice.utils.CommonUtils.checkAuthority;

@Service
public class BankomatSetService {

    @Autowired
    BankomatSetRepository bankomatSetRepository;

    public ApiResponse getAllBanknoteInfoByBankomatId(Integer bId) {
        if (checkAuthority(RoleName.ROLE_DIRECTOR)) {
            return new ApiResponse("You don't have the authority", false);
        }
        List<BankomatSet> bankomatSetList = bankomatSetRepository.findAllByBankomatId(bId);
        return new ApiResponse("Banknotes info",true,bankomatSetList);

    }

}
