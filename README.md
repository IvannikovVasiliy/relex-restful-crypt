# Restful-приложение для совершения операций на криптобирже

### Обзор
Приложение является RESTful API для совершения операций на бирже.

### Сервис позволяет выполнять следующие операции
* Регистрация пользователя
* Аутентификация
* Просмотр баланса своего кошелька
* Пополнение баланса своей карты
* Снятие денег с карты
* Просмтор актуальных курсов валют
* Обмен валютами
* Изменение администратором курса валют
* Просмотр администратором общей суммы на всех пользовательских счетах для указанной валюты
* Посмотр администратором количества операций, которые были проведены за указанный период

### Сервис реализует дополнительные требования
* Подключение базы данных PostgreSQL для хранения информации о балансе денег на кредитных картах, 
  балансе криптовалют на кошельках пользователей и истории операций
* сервис по запросу может возвращать данные в json ИЛИ xml.
* Формат может быть изменен добавлением header
* accept:application/json или accept:application/xml к HTTP-запросу.
* Использование Spring Security для разграничения ролей (admin/user)

### Использованные технологии
* Java 17
* Maven
* Spring Boot
* Spring Security
* Spring Validation
* JWT
* Lombok
* JPA
* PostgreSQL
* Liquibase
* Docker

## Запуск приложения

Перейти в корневую папку проекта и прописать команду

`mvn clean install`

Собрать докер образ и запустить контейнеры

`docker-compose up`

Перейти по адресу `localhost:5050` и осуществить вход

Email Adress / Username: `admin@admin.com` 

Password: `root`

Создать новый сервер для Postgres. Во вкладке `Connection` прописать следующие настройки

Host name/adress: `postgresdb`

Port: `5432`

Maintenance database: `crypt_exchanger`

Username: `postgres`

Password: `postgres`

Перейти в корень проекта и запустить созданный контейнер
`docker start crypt_relex`

---

## Endpoints

### Регистрация пользователя
Для получения токена доступа выполнить аутентификацию  
`POST /users/registration`
В случае размещения приложения на порте 8080, запрос будет выглядеть так

`localhost:8080/users/registration`

#### Заголовки
`accept: application/json` или `accept: application/xml`

#### Тело запроса
`{`  
` "username": "Ivanov",`  
` "email": "ivanov@gmail.com",`  
` "password": "ivanov"`  
`}`

#### Пример ответа
`User "Ivanov" is REGISTERED. Do login to get a secret key to be authenticated`

---

### Аутентификация

Для получения доступа к операциям на бирже необходимо 
полученный jwt-токен вставить в заголовок запроса

`POST /users/login`

#### Заголовки
`accept: application/json` или `accept: application/xml`

#### Тело запроса
`{`  
` "username": "Ivanov",`  
` "password": "ivanov"`  
`}`

#### Пример ответа
##### XML
`<Map>`  
`<jwt_token>eyJhb...</jwt_token>`  
`</Map>`
##### JSON
`{`  
`"jwt_token": "eyJhb..."`  
`}`

---

### Создать новую кредитую карту
`POST /wallet`

#### Заголовки
`accept: application/json` или `accept: application/xml`  
`Bearer Token: eyJhb...`

#### Пример ответа
`Wallet f8987821-935f-4b89-aa27-1c71a1314ca8 is CREATED`

---

### Создать новую кредитую карту
`POST /cards`

#### Заголовки
`accept: application/json` или `accept: application/xml`  
`Bearer Token: eyJhb...`

#### Пример ответа
`Card with number 7 is CREATED`

---

### Просмотр баланса
`GET /balance/my`

#### Заголовки
`accept: application/json` или `accept: application/xml`  
`Bearer Token: eyJhb...`

#### Пример ответа
##### XML
`<BalanceResponseDto>`  
`<RUB_wallet>810.0</RUB_wallet>`  
`<BTC_wallet>0.8999999999999999</BTC_wallet>`  
`<TON_waller>0.0</TON_waller>`  
`</BalanceResponseDto>`
##### JSON
`{`  
`"RUB_wallet": 810.0`    
`"BTC_wallet": 0.8999999999999999`  
`"TON_wallet" 90.0`  
`}`

---

### Положить деньги на карту

`PATCH /cards/push`

#### Заголовки
`accept: application/json` или `accept: application/xml`  
`Bearer Token: eyJhb...`

#### Тело запроса
`{`    
`"RUB_wallet": 100,`  
`"credit_card": "1"`  
`}`

#### Пример ответа
##### XML
`<MoneyResponseDto>`  
`<RUB_wallet>100.0</RUB_wallet>`   
`</MoneyResponseDto>`

##### JSON
`{`    
`"RUB_wallet": 100.0`  
`}`

---

### Снять деньги с карты

Снимать можно только деньги с карты.
При необходимости вывести крипту необходимо обменять крипту на деньги

`PATCH /cards/pop`

#### Заголовки
`accept: application/json` или `accept: application/xml`  
`Bearer Token: eyJhb...`

#### Тело запроса
`{`    
`"RUB_wallet": 100,`  
`"credit_card": "1"`  
`}`

#### Пример ответа
##### XML
`<MoneyResponseDto>`  
`<RUB_wallet>100.0</RUB_wallet>`   
`</MoneyResponseDto>`

##### JSON
`{`    
`"RUB_wallet": 100.0`  
`}`

---

### Просмотр актуальных курсов валют
Пользователь может запросить актуальные курсы валют.  
Для этого он в запросе должен указать тип валюты,  
относительно которой он хочет узнать курс.

`GET /currencies/actual`

#### Заголовки
`accept: application/json` или `accept: application/xml`  
`Bearer Token: eyJhb...`

#### Тело запроса
`{`  
`"currency": "ton"`
`}`

#### Пример ответа
##### XML
`<Map>`  
`<btc>0.007692307692307693</btc>`  
`<rub>11076.923076923076</rub>`
`</Map>`

##### JSON
`{`  
`"btc": 0.007692307692307693,`  
`"btc": 11076.923076923076`
`}`

---

### Обмен валют по установленному курсу.
Пользователь может обменять валюту,  
которая хранится у него на счетах по установленным курсам.   
Деньги(например, рубли) хранятся на карте, значит если мы хотим обменять деньги,  
значит необходимо указать номер кредитной карты, с которой будет производиться обмен.   
В запросе нужно указать валюту, которую меняем, её количество, и валюту,  
которую хотим получить.   
Например, мы хотим продать 0.01 BTC и получить за них рубли

`PATCH /balance/exchanger`

#### Заголовки
`accept: application/json` или `accept: application/xml`  
`Bearer Token: eyJhb...`

#### Тело запроса
`{`    
`"currency_from": "btc",`  
`"currency_to": "rub",`  
`"amount": 0.01,`  
`"credit_card": 1`  
`}`

#### Пример ответа
##### XML
`<ExchangeResponseDto>`  
`<currency_from>btc</currency_from>`  
`<currency_to>rub</currency_to>`  
`<amount_from>0.01</amount_from>`  
`<amount_to>14400.0</amount_to>`  
`</ExchangeResponseDto>`

##### JSON
`{`    
`"currency_from": "btc",`  
`"currency_to": "rub",`  
`"amount_from": 0.01,`  
`"amount_to": "14400.0"`  
`}`

---

### Изменить курс валют

`PATCH /currencies`

#### Заголовки
`accept: application/json` или `accept: application/xml`  
`Bearer Token: eyJhb...`

#### Тело запроса
`{`  
`"base_currency": "TON",`  
`"BTC": "0.000096",`  
`"RUB": "184"`  
`}`

#### Пример ответа
##### XML
`<Map>`  
`<BTC>0.000096</BTC>,`  
`<RUB>184</RUB>`  
`</Map>`

##### JSON
`{`  
`"BTC": "0.000096",`  
`"RUB": "184"`  
`}`


---

### Посмотреть общую сумму на всех пользовательских счетах для указанной валюты
Операция доступна только администратору  

`GET /balance/general-sum`

#### Заголовки
`accept: application/json` или `accept: application/xml`  
`Bearer Token: eyJhb...`

#### Тело запроса
`{`  
`"currency": "RUB"`  
`}`

#### Пример ответа
##### XML
`<Map>`  
`<rub>27.0</rub>`  
`</Map>`

##### JSON
`{`  
`"rub": 27.0`  
`}`

---

### Количество операций, которые были проведены за указанный период
Операция доступна только администратору  

`GET /admin/count-operations`

#### Заголовки
`accept: application/json` или `accept: application/xml`  
`Bearer Token: eyJhb...`

#### Тело запроса
`{`  
`"date_from": "2023-02-26 00:00:00",`  
`"password": "2023-02-26 07:40:00"`  
`}`

#### Пример ответа
##### XML
`<Map><transaction_count>27</transaction_count></Map>`
##### JSON
`{"transaction_count": 27}`