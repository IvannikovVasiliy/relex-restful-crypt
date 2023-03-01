package ru.relex.service.impl;

import org.slf4j.Logger;
import ru.relex.dto.CurrencyDto;
import ru.relex.entity.CurrencyEntity;
import ru.relex.exception.ResourceNotFoundException;
import ru.relex.repository.CurrencyRepository;
import ru.relex.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final Logger logger;

    public Map<String, Double> currencies(CurrencyDto currencyDto) {
        String relativeCurr = currencyDto.getCurrency().toLowerCase();
        if (currencyRepository.findByName(relativeCurr) == null) {
            throw new ResourceNotFoundException(String.format("The currency \"%s\" does not exist", relativeCurr));
        }

        List<CurrencyEntity> currencies = currencyRepository.findAll().stream()
                .filter(currencyEntity -> !currencyEntity.getName().equals(relativeCurr))
                .toList();

        Map<String, Double> map = new HashMap<>();
        for (var curr : currencies) {
            double rate = convertCurrencies(currencyDto.getCurrency(), curr.getName());
            map.put(curr.getName(), rate);
        }

        return map;
    }

    private Double convertCurrencies(String relativeCurrency, String targetCurrency) {
        double relative = currencyRepository.findByName(relativeCurrency.toLowerCase()).getValue();
        double target = currencyRepository.findByName(targetCurrency).getValue();
        double res = target / relative;
        return res;
    }

    @Transactional
    @Validated
    public Map<String, Double> editCurrency(Map<String, String> map) {
        Map<String, Double> res = new HashMap<>();

        String baseCurrencyString = map.get("base_currency").toLowerCase();
        map.remove("base_currency");

        CurrencyEntity currencyEntity = currencyRepository.findByName(baseCurrencyString);
        if (currencyEntity == null) {
            throw new ResourceNotFoundException(String.format("The currency \"%s\" does not exist", baseCurrencyString));
        }
        currencyEntity.setValue(1.0);

        currencyRepository.save(currencyEntity);
        logger.info(
                "Set the course {} for currency \"{}\"",
                currencyEntity.getValue(), currencyEntity.getName()
        );


        for (var curr : map.keySet()) {
            currencyEntity = currencyRepository.findByName(curr.toLowerCase());
            if (currencyEntity == null) {
                throw new ResourceNotFoundException(String.format("The currency \"%s\" does not exist", curr));
            }
            currencyEntity.setValue(Double.valueOf(map.get(curr)));
            currencyRepository.save(currencyEntity);
            logger.info(
                    "Set the course {} for currency \"{}\"",
                    currencyEntity.getValue(), currencyEntity.getName()
            );

            res.put(curr, Double.valueOf(map.get(curr)));
        }

        return res;
    }
}
