package uz.mk.atmservice.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MoneyDto {
    @NotNull
    private Integer banknoteId;

    @NotNull
    private Integer amount;

    @NotNull
    private Integer bankomatId;

    private Integer accountTypeId;


}
