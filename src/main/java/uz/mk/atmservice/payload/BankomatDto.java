package uz.mk.atmservice.payload;

import lombok.Data;
import uz.mk.atmservice.entity.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
public class BankomatDto {
    private Double maxMoney;

    private Double minMoney;

    private Double balance;

    @NotNull
    private Integer commissionSetId;

    @NotNull
    private String place;

    @NotNull
    private String street;

    @NotNull
    private String district;

    @NotNull
    private Integer bankId;

    private List<Integer> cardTypeIds;

    private List<Integer> banknoteIds;

}
