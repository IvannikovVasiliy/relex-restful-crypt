package ru.relex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class ExchangeResponseDto {
    @JsonProperty("currency_from")
    private String currencyFrom;
    @JsonProperty("currency_to")
    private String currencyTo;
    @JsonProperty("amount_from")
    private Double amountFrom;
    @JsonProperty("amount_to")
    private Double amountTo;
}
