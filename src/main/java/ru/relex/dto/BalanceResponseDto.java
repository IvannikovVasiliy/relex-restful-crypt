package ru.relex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BalanceResponseDto {
    @JsonProperty("RUB_wallet")
    private double rubWallet;
    @JsonProperty("BTC_wallet")
    private double btcWallet;
    @JsonProperty("TON_wallet")
    private double tonWallet;
}
