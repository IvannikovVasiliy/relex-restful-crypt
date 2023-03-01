package ru.relex.controller;

import org.springframework.http.HttpHeaders;
import ru.relex.config.CustomHttpHeader;
import ru.relex.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/wallet", produces = {"application/json", "application/xml"})
public class WalletController {

    private final WalletService walletService;

    @PostMapping
    public ResponseEntity<String> createWallet(Principal principal) {
        UUID wallet = walletService.createWallet(principal);

        HttpHeaders headers = CustomHttpHeader.getHttpHeader();
        return new ResponseEntity(
                String.format("Wallet %s is CREATED", wallet),
                headers,
                HttpStatus.CREATED);
    }
}
