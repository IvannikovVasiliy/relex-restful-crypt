package ru.relex.controller;

import org.springframework.http.HttpHeaders;
import ru.relex.config.CustomHttpHeader;
import ru.relex.dto.MoneyResponseDto;
import ru.relex.dto.MoneyRequestDto;
import ru.relex.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/cards", produces = {"application/json", "application/xml"})
public class CardController {

    private final CardService cardService;

    @PostMapping
    public ResponseEntity<String> createCard(Principal principal) {
        Long numberCard = cardService.createCard(principal);

        HttpHeaders headers = CustomHttpHeader.getHttpHeader();
        return new ResponseEntity<>(
                String.format("Card with number %d is CREATED", numberCard),
                headers,
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/push")
    public ResponseEntity<MoneyResponseDto> pushMoneyToWallet(Principal principal,
                                              @Valid @RequestBody MoneyRequestDto moneyDto) {
        HttpHeaders headers = CustomHttpHeader.getHttpHeader();

        return new ResponseEntity<>(
                cardService.pushMoneyToWallet(principal, moneyDto),
                headers,
                HttpStatus.OK
        );
    }

    @PatchMapping("/pop")
    public ResponseEntity<MoneyResponseDto> popMoney(Principal principal,
                                     @Valid @RequestBody MoneyRequestDto moneyDto) {
        HttpHeaders headers = CustomHttpHeader.getHttpHeader();

        return new ResponseEntity<>(
                cardService.popMoney(principal, moneyDto),
                headers,
                HttpStatus.OK
        );
    }
}
