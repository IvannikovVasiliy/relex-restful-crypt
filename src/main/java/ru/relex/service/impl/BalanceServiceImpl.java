package ru.relex.service.impl;

import org.slf4j.Logger;
import ru.relex.dto.BalanceResponseDto;
import ru.relex.dto.CurrencyDto;
import ru.relex.dto.ExchangeCurrencyRequestDto;
import ru.relex.dto.ExchangeResponseDto;
import ru.relex.entity.CardEntity;
import ru.relex.entity.UserEntity;
import ru.relex.entity.WalletEntity;
import ru.relex.exception.AccessDeniedException;
import ru.relex.exception.ResourceNotFoundException;
import ru.relex.model.ExchangeCurrencyModel;
import ru.relex.repository.*;
import ru.relex.service.BalanceService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class BalanceServiceImpl implements BalanceService {

    private final EntityManager em;
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final CardRepository cardRepository;
    private final HistoryOperationRepository historyOperationRepository;
    private final WalletRepository walletRepository;
    private final Logger logger;

    @Transactional(readOnly = true)
    public BalanceResponseDto myBalance(Principal principal) {
        UserEntity userEntity = userRepository.findByUsername(principal.getName());

        WalletEntity walletEntity = userEntity.getWallet();
        double btc = 0.0;
        double ton = 0.0;
        if (walletEntity != null) {
            btc = walletEntity.getBtc();
            ton = walletEntity.getTon();
        }

        double sumRub = userEntity.getCards().stream()
                .map(card -> card.getRub())
                .reduce((c1, c2) -> c1+c2)
                .orElse(0.0);

        return BalanceResponseDto.builder()
                .rubWallet(sumRub)
                .btcWallet(btc)
                .tonWallet(ton)
                .build();
    }

    public Map<String, Double> sum(CurrencyDto currencyDto) {
        String query;
        if (currencyDto.getCurrency().toLowerCase().equals("rub")) {
            query = "select sum(rub) from CardEntity";
        } else {
            query = String.format("select sum(%s) from WalletEntity", currencyDto.getCurrency().toLowerCase());
        }
        Double sum;
        try {
            sum = (Double) em.createQuery(query).getSingleResult();
        } catch (Exception e) {
            throw new ResourceNotFoundException(
                    String.format("The currency \"%s\" is not found", currencyDto.getCurrency())
            );
        }
        Map<String, Double> res = new HashMap<>();
        res.put(currencyDto.getCurrency(), sum);
        return res;
    }

    @Validated
    public ExchangeResponseDto exchanger(Principal principal, ExchangeCurrencyRequestDto exchangeCurrDto) {
        UserEntity userEntity = userRepository.findByUsername(principal.getName());
        CardEntity cardEntity = null;
        if (exchangeCurrDto.getCreditCard() != null) {
            cardEntity = userEntity.getCards().stream()
                    .filter(c -> c.getId() == exchangeCurrDto.getCreditCard())
                    .findAny()
                    .orElseThrow(() -> new AccessDeniedException("Access denied to the credit card"));
        }

        WalletEntity walletEntity;
        if (userEntity.getWallet() == null) {
            throw new ResourceNotFoundException(String.format(
                    "Wallet for user \"%s\" does not exist. Try to create wallet.",
                    principal.getName()
            ));
        } else {
            walletEntity = userEntity.getWallet();
        }

        ExchangeCurrencyModel sellModel = ExchangeCurrencyModel
                .builder()
                .currency(exchangeCurrDto.getCurrencyFrom().toLowerCase())
                .amount(exchangeCurrDto.getAmount())
                .cardEntity(cardEntity)
                .walletEntity(walletEntity)
                .build();
        ExchangeCurrencyModel resultSelling = sellCurrency(sellModel);
        historyOperationRepository.sell(resultSelling);
        logger.info(
                "The user {} sell {} {}",
                userEntity.getUsername(), resultSelling.getAmount(), resultSelling.getCurrency()
        );

        ExchangeCurrencyModel buyModel = ExchangeCurrencyModel
                .builder()
                .currency(exchangeCurrDto.getCurrencyTo().toLowerCase())
                .amount(exchangeCurrDto.getAmount())
                .cardEntity(cardEntity)
                .walletEntity(walletEntity)
                .build();
        ExchangeCurrencyModel resultBuying = buyCurrency(exchangeCurrDto, cardEntity, walletEntity);
        historyOperationRepository.buy(resultBuying);
        logger.info(
                "The user \"{}\" buy {} {}",
                userEntity.getUsername(), resultBuying.getAmount(), resultBuying.getCurrency()
        );

        return ExchangeResponseDto
                .builder()
                .currencyFrom(exchangeCurrDto.getCurrencyFrom())
                .currencyTo(exchangeCurrDto.getCurrencyTo())
                .amountFrom(resultSelling.getAmount())
                .amountTo(resultBuying.getAmount())
                .build();
    }

    private ExchangeCurrencyModel sellCurrency(ExchangeCurrencyModel exchangeCurr) {
        WalletEntity wallet = exchangeCurr.getWalletEntity();
        CardEntity card = exchangeCurr.getCardEntity();
        switch (exchangeCurr.getCurrency()) {
            case "rub":
                if (card == null) {
                    throw new ResourceNotFoundException(
                            String.format("The credit card with number %d does not exist", card.getId())
                    );
                }
                card.setRub(card.getRub() - exchangeCurr.getAmount());
                cardRepository.save(card);
                break;
            case "ton":
                wallet.setTon(wallet.getTon() - exchangeCurr.getAmount());
                walletRepository.save(wallet);
                break;
            case "btc":
                wallet = exchangeCurr.getWalletEntity();
                wallet.setBtc(wallet.getBtc() - exchangeCurr.getAmount());
                walletRepository.save(wallet);
                break;
        }

        return ExchangeCurrencyModel.builder()
                .amount(exchangeCurr.getAmount())
                .currency(exchangeCurr.getCurrency())
                .cardEntity(card)
                .walletEntity(wallet)
                .build();
    }

    private ExchangeCurrencyModel buyCurrency(ExchangeCurrencyRequestDto model, CardEntity cardEntity, WalletEntity walletEntity) {
        double rateRelativeCurr = currencyRepository.findByName(model.getCurrencyFrom().toLowerCase()).getValue();
        double rateTargetCurr = currencyRepository.findByName(model.getCurrencyTo().toLowerCase()).getValue();
        double push = model.getAmount() * rateTargetCurr / rateRelativeCurr;

        switch (model.getCurrencyTo()) {
            case "rub":
                if (cardEntity == null) {
                    throw new ResourceNotFoundException(
                            String.format("The credit card with number %d", model.getCreditCard())
                    );
                }
                cardEntity.setRub(cardEntity.getRub() + push);
                break;
            case "ton":
                walletEntity.setTon(walletEntity.getTon() + push);
                break;
            case "btc":
                walletEntity.setBtc(walletEntity.getBtc() + push);
                break;
        }
        walletRepository.save(walletEntity);

        return ExchangeCurrencyModel.builder()
                .amount(push)
                .currency(model.getCurrencyTo())
                .cardEntity(cardEntity)
                .walletEntity(walletEntity)
                .build();
    }
}
