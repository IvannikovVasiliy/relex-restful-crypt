package ru.relex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.relex.valid.constraint.AmountConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ExchangeCurrencyRequestDto {
    @JsonProperty("currency_from")
    @NotBlank
    private String currencyFrom;

    @JsonProperty("currency_to")
    @NotBlank
    private String currencyTo;

    @AmountConstraint
    private Double amount;

    @JsonProperty("credit_card")
    private Long creditCard;
}
