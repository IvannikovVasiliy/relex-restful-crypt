package ru.relex.service;

import ru.relex.dto.MoneyRequestDto;
import ru.relex.dto.MoneyResponseDto;

import java.security.Principal;

public interface CardService {
    Long createCard(Principal principal);
    MoneyResponseDto pushMoneyToWallet(Principal principal, MoneyRequestDto moneyDto);
    MoneyResponseDto popMoney(Principal principal, MoneyRequestDto moneyDto);
}
