package ru.relex.service.impl;

import org.slf4j.Logger;
import ru.relex.dto.MoneyRequestDto;
import ru.relex.dto.MoneyResponseDto;
import ru.relex.entity.CardEntity;
import ru.relex.entity.UserEntity;
import ru.relex.exception.AccessDeniedException;
import ru.relex.exception.ResourceNotFoundException;
import ru.relex.repository.CardRepository;
import ru.relex.repository.HistoryOperationRepository;
import ru.relex.repository.UserRepository;
import ru.relex.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.security.Principal;

@Service
@RequiredArgsConstructor
@Transactional
public class CardServiceImpl implements CardService {

    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final HistoryOperationRepository historyOperationRepository;
    private final Logger logger;

    public Long createCard(Principal principal) {
        CardEntity cardEntity = new CardEntity();
        UserEntity userEntity = userRepository.findByUsername(principal.getName());
        cardEntity.setUser(userEntity);
        cardRepository.save(cardEntity);
        logger.info("Thr card {} was CREATED", cardEntity.getId());

        return cardEntity.getId();
    }

    public MoneyResponseDto pushMoneyToWallet(Principal principal, MoneyRequestDto moneyDto) {
        Long creditCardId = moneyDto.getCreditCard();
        CardEntity cardEntity = cardRepository.findById(creditCardId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("The credit card %d not found", creditCardId)));

        UserEntity userEntity = cardEntity.getUser();
        if (!userEntity.getUsername().equals(principal.getName())) {
            logger.warn(
                    "Access denied. The user {} try to get access the card {}",
                    userEntity.getUsername(), moneyDto.getCreditCard()
            );
            throw new AccessDeniedException("Access denied");
        }

        Double pushValue = moneyDto.getRubWallet();
        cardEntity.setRub(cardEntity.getRub() + pushValue);
        cardRepository.save(cardEntity);

        historyOperationRepository.push(pushValue, cardEntity);

        logger.info(
                "The user {} push {} rubles to the card {}",
                userEntity.getUsername(), moneyDto.getRubWallet(), moneyDto.getCreditCard()
        );

        MoneyResponseDto responseDto = new MoneyResponseDto();
        responseDto.setRubWallet(pushValue);

        return responseDto;
    }

    @Validated
    public MoneyResponseDto popMoney(Principal principal, MoneyRequestDto moneyDto) {
        Long creditCardId = moneyDto.getCreditCard();
        CardEntity cardEntity = cardRepository.findById(creditCardId).orElseThrow(() -> new ResourceNotFoundException(
                String.format("The card with number %d does not exist", moneyDto.getCreditCard())
        ));

        UserEntity userEntity = cardEntity.getUser();
        if (!userEntity.getUsername().equals(principal.getName())) {
            logger.warn(
                    "Access denied. The user {} try to get access the card {}",
                    userEntity.getUsername(), moneyDto.getCreditCard()
            );
            throw new AccessDeniedException("Access denied");
        }

        Double popValue = moneyDto.getRubWallet();
        cardEntity.setRub(cardEntity.getRub() - popValue);
        cardRepository.save(cardEntity);

        historyOperationRepository.pop(popValue, cardEntity);

        logger.info(
                "The user {} pop {} rubles from the card {}",
                userEntity.getUsername(), moneyDto.getRubWallet(), moneyDto.getCreditCard()
        );

        MoneyResponseDto responseDto = new MoneyResponseDto();
        responseDto.setRubWallet(popValue);

        return responseDto;
    }
}
