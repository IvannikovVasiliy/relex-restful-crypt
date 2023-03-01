package ru.relex.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import ru.relex.config.CustomHttpHeader;
import ru.relex.dto.CurrencyDto;
import ru.relex.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/currencies", produces = {"application/json", "application/xml"})
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping(value = "/actual")
    public ResponseEntity<Map<String, Double>> getCurrencies(@RequestBody CurrencyDto currencyDto) {
        HttpHeaders headers = CustomHttpHeader.getHttpHeader();

        return new ResponseEntity<>(
                currencyService.currencies(currencyDto),
                headers,
                HttpStatus.OK
        );
    }

    @PatchMapping
    public ResponseEntity<Map<String, Double>> editCurrency(@RequestBody Map<String, String> map) {
        HttpHeaders headers = CustomHttpHeader.getHttpHeader();

        return new ResponseEntity<>(
                currencyService.editCurrency(map),
                headers,
                HttpStatus.OK
        );
    }
}
