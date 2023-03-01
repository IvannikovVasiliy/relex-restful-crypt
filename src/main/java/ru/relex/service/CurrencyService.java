package ru.relex.service;

import ru.relex.dto.CurrencyDto;

import java.util.Map;

public interface CurrencyService {
    Map<String, Double> currencies(CurrencyDto currencyDto);
    Map<String, Double> editCurrency(Map<String, String> map);
}
