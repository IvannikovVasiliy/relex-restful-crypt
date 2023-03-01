package ru.relex.repository;

import ru.relex.model.ExchangeCurrencyModel;
import ru.relex.repository.dao.HistoryOperationDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.relex.entity.CardEntity;
import ru.relex.entity.CurrencyEntity;
import ru.relex.entity.HistoryOperationEntity;
import ru.relex.entity.OperationEntity;

@Repository
@RequiredArgsConstructor
public class HistoryOperationRepository {

    private final OperationRepository operationRepository;
    private final HistoryOperationDao historyOperationDao;
    private final CurrencyRepository currencyRepository;

    public void push(double push, CardEntity cardEntity) {
        OperationEntity operationEntity = operationRepository.findByDescription("push");
        HistoryOperationEntity historyOperationEntity = new HistoryOperationEntity();
        historyOperationEntity.setValue(push);
        historyOperationEntity.setOperation(operationEntity);
        historyOperationEntity.setCard(cardEntity);
        historyOperationEntity.setCurrency(currencyRepository.findByName("rub"));
        historyOperationDao.save(historyOperationEntity);
    }

    public void pop(double value, CardEntity cardEntity) {
        OperationEntity operationEntity = operationRepository.findByDescription("pop");
        CurrencyEntity currencyEntity = currencyRepository.findByName("rub");
        HistoryOperationEntity popOperation = new HistoryOperationEntity();
        popOperation.setValue(value);
        popOperation.setOperation(operationEntity);
        popOperation.setCard(cardEntity);
        popOperation.setCurrency(currencyEntity);
        historyOperationDao.save(popOperation);
    }

    public void buy(ExchangeCurrencyModel model) {
        OperationEntity operationEntity = operationRepository.findByDescription("buy");
        CurrencyEntity currencyEntity = currencyRepository.findByName(model.getCurrency().toLowerCase());
        HistoryOperationEntity buyOperation = new HistoryOperationEntity();
        buyOperation.setValue(model.getAmount());
        buyOperation.setOperation(operationEntity);
        buyOperation.setCurrency(currencyEntity);

        if (model.getCurrency().toLowerCase().equals("rub")) {
            buyOperation.setCard(model.getCardEntity());
        } else {
            buyOperation.setWallet(model.getWalletEntity());
        }
        historyOperationDao.save(buyOperation);
    }

    public void sell(ExchangeCurrencyModel model) {
        OperationEntity operationEntity = operationRepository.findByDescription("sell");
        CurrencyEntity currencyEntity = currencyRepository.findByName(model.getCurrency().toLowerCase());
        HistoryOperationEntity sellOperation = new HistoryOperationEntity();
        sellOperation.setValue(model.getAmount());
        sellOperation.setOperation(operationEntity);
        sellOperation.setCurrency(currencyEntity);

        if (model.getCurrency().toLowerCase().equals("rub")) {
            sellOperation.setCard(model.getCardEntity());
        } else {
            sellOperation.setWallet(model.getWalletEntity());
        }
        historyOperationDao.save(sellOperation);
    }
}
