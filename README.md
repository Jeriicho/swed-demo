# Demo project for Swedbank

## Description
A simple back-end containerized application that consist of:

- #### Account Service
  - Handles logic related to accounts.
- #### Balance Service
  - Handles depositing, withdrawing, exchange and transactions.
- #### H2 database

## Issues and things to consider
- Account Service is not really needed in this application, as the assignment
was only focused on transactional logic. However in a real-world scenario 
it would have its own microservice. And I also used it to demonstrate the external system call from balance service.
- Every service would have its own git repository in production. Currently they
are nested as different maven modules.

Things that I'm aware of, but didn't have time to go over:

- Exception handling sometimes returns 400 while it should return 404. 
Due to jakarta.validation throwing the same validation error, but in some cases it would 
be more accurate to mark it as 404. E.g. trying to withdraw from an account with 
a currency, that it does not have a balance related to yet.
- No tests
- /account/{account_id}/balance/withdraw —> would be more consistent to refactor to /account/{account_id}/balance/{currency_code}/withdraw



## Setup Instructions
### Requirements

- Maven
- Java v17
- Docker

In project root:
install the deps and compile the project.
```bash
  mvn clean install
```
Build docker images
```bash
  docker compose build --no-cache
```
Start the services
```bash
  docker compose up
```
Access the services from

| Service         | Access port |
|:----------------|:------------|
| Balance Service | localhost:**8082**    |
| Account Service | localhost:**8081**    |
Some test data is created on startup that is referenced in API reference examples.

## API Reference

### Account Service

#### Get account information

```http
  GET account/{account_id}
```
##### URL params

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `account_id` | `long` | **Required**. Account ID |
##### Response

| Field                | Type     | Description                   |
|:---------------------|:---------|:------------------------------|
| `id`                 | `number` | Account ID                    |
| `identificationCode` | `number` | Account's identification code |
| `name`               | `string` | Account name.                 |

#### Example request - response

```
  GET localhost:8081/account/1
```
```
  {
    "id": 1,
    "identificationCode": 12345,
    "name": "Test 1"
  }
```

### Balance Service
#### Get account balance

```http
  GET /account/{account_id}/balance
```
##### URL params

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `account_id`      | `long` | **Required**. Account ID |

##### Response
List of objects with fields: 

| Field      | Type     | Description   |
|:-----------|:---------|:--------------|
| `currency` | `string` | Currency code |
| `amount`   | `number` | Amount        |

#### Example request - response

```
  GET localhost:8082/account/1/balance
```
```
[
    {
        "currency": "EUR",
        "amount": 1000.00
    },
    {
        "currency": "USD",
        "amount": 500.00
    }
]
```

#### Get account balance for a specific currency

```http
  GET /account/{account_id}/balance/{currency_code}
```
##### URL params

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `account_id`      | `long` | **Required**. Account ID |
| `currency_code`      | `string` | **Required**. Currency code. Supported are EUR, USD, SEK, RUB |

##### Response

| Field      | Type     | Description   |
|:-----------|:---------|:--------------|
| `currency` | `string` | Currency code |
| `amount`   | `number` | Amount        |

#### Example request - response
```
  GET localhost:8082/account/1/balance/EUR
```
```
  {
    "currency": "EUR",
    "amount": 1000.00
  }
```

#### Deposit funds

```http
  POST /account/{account_id}/balance/deposit
```
##### URL params

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `account_id`      | `long` | **Required**. Account ID |

##### Request body

| Field | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `currency`      | `string` | **Required**. Currency code. Supported are EUR, USD, SEK, RUB|
| `amount`      | `number` | **Required**. Amount to deposit. Must be >= 0.1|

##### Response
No response body. Only by status.

#### Example request endpoint and body
```
  POST localhost:8082/account/1/balance/deposit
```
```
  {
    "currency":"EUR",
    "amount": 100
  }
```

#### Withdraw funds

```http
  POST /account/{account_id}/balance/withdraw
```
##### URL params

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `account_id`      | `long` | **Required**. Account ID |

##### Request body

| Field | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `currency`      | `string` | **Required**. Currency code. Supported are EUR, USD, SEK, RUB|
| `amount`      | `number` | **Required**. Amount to withdraw. Must be >= 0.1|

##### Response
No response body. Only by status.

#### Example request endpoint and body
```
  POST localhost:8082/account/1/balance/withdraw
```
```
  {
    "currency":"EUR",
    "amount": 10
  }
```

#### Exchange currencies

```http
  POST /account/{account_id}/balance/exchange
```
##### URL params

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `account_id`      | `long` | **Required**. Account ID |

##### Request body

| Field | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `fromCurrency`      | `string` | **Required**. Currency code to exchange from. Supported are EUR, USD, SEK, RUB|
| `toCurrency`      | `string` | **Required**. Currency code to exchange to. Supported are EUR, USD, SEK, RUB|
| `amount`      | `number` | **Required**. Amount to exchange. Must be >= 1|

##### Response
No response body. Only by status.

#### Example request endpoint and body
```
  POST localhost:8082/account/1/balance/exchange
```
```
  {
    "fromCurrency": "EUR",
    "toCurrency": "USD",
    "amount": 1
  }
```

#### List past transactions for an account

```http
  GET /account/{account_id}/transaction
```
##### URL params

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `account_id`      | `long` | **Required**. Account ID |

##### Response
List of objects with following fields:

| Field                | Type        | Description                               |
|:---------------------|:------------|:------------------------------------------|
| `accountIdentifier`| `number`    | Account's identification code             |
| `accountName` | `string`    | Account name                              |
| `type`               | `string`    | Transaction type (DEPOSIT/DEBIT/EXCHANGE) |
| `currency`               | `string`    | Currency code                             |
| `amount`               | `number`    | Amount                                    |
| `timestamp`               | `timestamp` | Timestamp of transaction                  |


#### Example request - response

```
  GET localhost:8082/account/1/transaction
```
```
  [
    {
        "accountIdentifier": 12345,
        "accountName": "Test 1",
        "type": "DEPOSIT",
        "currency": "EUR",
        "amount": 100.00,
        "timestamp": "2025-05-25T20:59:23.220504"
    },
    {
        "accountIdentifier": 12345,
        "accountName": "Test 1",
        "type": "DEBIT",
        "currency": "EUR",
        "amount": 10.00,
        "timestamp": "2025-05-25T21:07:15.120513"
    },
    {
        "accountIdentifier": 12345,
        "accountName": "Test 1",
        "type": "EXCHANGE",
        "currency": "USD",
        "amount": 1.00,
        "timestamp": "2025-05-25T21:08:25.334401"
    }
]
```