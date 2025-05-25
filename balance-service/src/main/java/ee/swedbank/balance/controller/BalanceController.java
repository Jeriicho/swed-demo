package ee.swedbank.balance.controller;

import ee.swedbank.balance.dto.BalanceDto;
import ee.swedbank.balance.dto.CurrencyExchangeRequest;
import ee.swedbank.balance.model.CurrencyType;
import ee.swedbank.common.exception.ResourceNotFoundException;
import ee.swedbank.balance.model.TransactionType;
import ee.swedbank.balance.service.BalanceService;
import ee.swedbank.balance.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account/{accountId}/balance")
public class BalanceController {

    private final BalanceService balanceService;
    private final TransactionService transactionService;

    public BalanceController(BalanceService balanceService, TransactionService transactionService) {
        this.balanceService = balanceService;
        this.transactionService = transactionService;
    }

    @GetMapping
    public List<BalanceDto> getBalancesByAccountId(@PathVariable("accountId") long id) {
        List<BalanceDto> balanceDtos = this.balanceService.findBalancesByAccountId(id);
        if (balanceDtos.isEmpty()) {
            throw new ResourceNotFoundException("No balances with account id " + id + " found");
        }
        return balanceDtos;
    }

    @GetMapping(value = "/{currencyCode}")
    public BalanceDto getBalanceByAccountIdAndCurrency(@PathVariable("accountId") long id,
                                                       @PathVariable CurrencyType currencyCode) {
        return this.balanceService.getBalanceByAccountIdAndCurrency(id, currencyCode);
    }

    @PostMapping(value = "/deposit")
    public void deposit(@PathVariable("accountId") Long accountId, @RequestBody @Valid BalanceDto balanceDto) {
        BalanceDto depositedBalance = this.balanceService.deposit(accountId, balanceDto.getCurrency(), balanceDto.getAmount());
        this.transactionService.recordTransaction(accountId, TransactionType.DEPOSIT, depositedBalance.getCurrency(), depositedBalance.getAmount(), balanceDto.getAmount());
    }

    @PostMapping(value = "/withdraw")
    public void withdraw(@PathVariable("accountId") long accountId, @RequestBody @Valid BalanceDto balanceDto) {
        BalanceDto withdrawnBalance = this.balanceService.withdraw(accountId, balanceDto.getCurrency(), balanceDto.getAmount());
        this.transactionService.recordTransaction(accountId, TransactionType.DEBIT, withdrawnBalance.getCurrency(), withdrawnBalance.getAmount(), balanceDto.getAmount());
    }

    @PostMapping(value = "/exchange")
    public void exchange(@PathVariable("accountId") long accountId, @RequestBody @Valid CurrencyExchangeRequest exchangeRequest) {
        BalanceDto exchangedBalance = this.balanceService.exchange(accountId, exchangeRequest.getFromCurrency(), exchangeRequest.getToCurrency(), exchangeRequest.getAmount());
        this.transactionService.recordTransaction(accountId, TransactionType.EXCHANGE, exchangedBalance.getCurrency(), exchangedBalance.getAmount(), exchangeRequest.getAmount());
    }

}
