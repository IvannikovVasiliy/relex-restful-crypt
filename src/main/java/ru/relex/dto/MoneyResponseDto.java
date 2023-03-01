package ru.relex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MoneyResponseDto {
    @JsonProperty("RUB_wallet")
    private Double rubWallet;
}
