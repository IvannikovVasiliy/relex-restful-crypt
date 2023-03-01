package ru.relex.model;

import ru.relex.entity.CardEntity;
import ru.relex.entity.WalletEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExchangeCurrencyModel {
    private double amount;
    private String currency;
    private CardEntity cardEntity;
    private WalletEntity walletEntity;
}
