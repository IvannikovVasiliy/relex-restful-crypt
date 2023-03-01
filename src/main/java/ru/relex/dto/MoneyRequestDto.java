package ru.relex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.relex.valid.constraint.AmountConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MoneyRequestDto {
    @JsonProperty("RUB_wallet")
    @AmountConstraint
    private Double rubWallet;

    @JsonProperty("credit_card")
    @NotNull
    private Long creditCard;
}
