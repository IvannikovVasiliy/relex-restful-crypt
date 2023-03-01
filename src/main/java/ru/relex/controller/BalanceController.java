package ru.relex.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.relex.config.CustomHttpHeader;
import ru.relex.dto.CurrencyDto;
import ru.relex.dto.BalanceResponseDto;
import ru.relex.dto.ExchangeCurrencyRequestDto;
import ru.relex.dto.ExchangeResponseDto;
import ru.relex.service.BalanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/balance", produces = {"application/json", "application/xml"})
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping("/my")
    public ResponseEntity<BalanceResponseDto> myBalance(Principal principal) {
        HttpHeaders headers = CustomHttpHeader.getHttpHeader();

        return new ResponseEntity<>(
                balanceService.myBalance(principal),
                headers,
                HttpStatus.OK
        );
    }

    @GetMapping("/general-sum")
    public ResponseEntity<Map<String, Double>> sum(@Valid @RequestBody CurrencyDto currencyDto) {
        HttpHeaders headers = CustomHttpHeader.getHttpHeader();

        return new ResponseEntity<>(
                balanceService.sum(currencyDto),
                headers,
                HttpStatus.OK
        );
    }

    @PatchMapping("/exchanger")
    public ResponseEntity<ExchangeResponseDto> exchanger(Principal principal, @Valid @RequestBody ExchangeCurrencyRequestDto exchangeCurrencyRequestDto) {
        HttpHeaders headers = CustomHttpHeader.getHttpHeader();

        return new ResponseEntity<>(
                balanceService.exchanger(principal, exchangeCurrencyRequestDto),
                headers,
                HttpStatus.OK
        );
    }
}
