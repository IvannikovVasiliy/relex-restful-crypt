package ru.relex.service;

import ru.relex.dto.BalanceResponseDto;
import ru.relex.dto.CurrencyDto;
import ru.relex.dto.ExchangeCurrencyRequestDto;
import ru.relex.dto.ExchangeResponseDto;

import java.security.Principal;
import java.util.Map;

public interface BalanceService {

    BalanceResponseDto myBalance(Principal principal);
    Map<String, Double> sum(CurrencyDto currencyDto);
    ExchangeResponseDto exchanger(Principal principal, ExchangeCurrencyRequestDto exchangeCurrDto);
}
