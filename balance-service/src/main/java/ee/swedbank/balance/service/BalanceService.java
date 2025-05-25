package ee.swedbank.balance.service;

import ee.swedbank.balance.client.AccountClient;
import ee.swedbank.balance.dto.BalanceDto;
import ee.swedbank.common.dto.AccountDto;
import ee.swedbank.common.exception.ResourceNotFoundException;
import ee.swedbank.balance.mapper.BalanceMapper;
import ee.swedbank.balance.model.Balance;
import ee.swedbank.balance.model.CurrencyType;
import ee.swedbank.balance.repository.BalanceRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BalanceService {

    private final BalanceRepository balanceRepository;
    private final AccountClient accountClient;
    private final BalanceMapper balanceMapper;
    private final CurrencyConversionService currencyConversionService;

    public BalanceService(BalanceRepository balanceRepository,
                          BalanceMapper balanceMapper,
                          CurrencyConversionService currencyConversionService,
                          AccountClient accountClient) {
        this.balanceRepository = balanceRepository;
        this.balanceMapper = balanceMapper;
        this.currencyConversionService = currencyConversionService;
        this.accountClient = accountClient;
    }

    public BalanceDto getBalanceByAccountIdAndCurrency(long accountId, CurrencyType currency) {
        return this.balanceMapper.toBalanceDto(this.balanceRepository.getBalanceByAccountIdAndCurrency(accountId, currency)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Balance not found for account id " + accountId + " and currency " + currency)
                )
        );
    }

    public List<BalanceDto> findBalancesByAccountId(Long accountId) {
        return this.balanceMapper.toBalanceDtos(this.balanceRepository.findBalancesByAccountId(accountId));
    }

    public BalanceDto deposit(Long accountId, CurrencyType currencyType, BigDecimal amount) {
        Optional<Balance> existingBalance = this.balanceRepository.getBalanceByAccountIdAndCurrency(accountId, currencyType);
        if (existingBalance.isPresent()) {
            Balance balanceToDeposit = existingBalance.get();
            balanceToDeposit.setAmount(balanceToDeposit.getAmount().add(amount));
            return this.balanceMapper.toBalanceDto(this.balanceRepository.saveAndFlush(balanceToDeposit));
        }
        AccountDto accountDto = this.accountClient.getAccountById(accountId);
        Balance balanceToDeposit = new Balance();
        balanceToDeposit.setAccountId(accountDto.getId());
        balanceToDeposit.setAmount(amount);
        balanceToDeposit.setCurrency(currencyType);
        return this.balanceMapper.toBalanceDto(this.balanceRepository.saveAndFlush(balanceToDeposit));
    }

    public BalanceDto withdraw(Long accountId, CurrencyType currencyType, BigDecimal amount) {
        Optional<Balance> balance = this.balanceRepository.getBalanceByAccountIdAndCurrency(accountId, currencyType);
        if (balance.isPresent()) {
            Balance balanceToWithdraw = balance.get();
            if (balanceToWithdraw.getAmount().compareTo(amount) < 0) {
                throw new IllegalArgumentException("Trying to withdraw more than amount of balance");
            }
            balanceToWithdraw.setAmount(balanceToWithdraw.getAmount().subtract(amount));
            return this.balanceMapper.toBalanceDto(this.balanceRepository.saveAndFlush(balanceToWithdraw));
        }
        throw new IllegalArgumentException("No such balance");
    }

    public BalanceDto exchange(Long accountId, CurrencyType fromCurrency, CurrencyType toCurrency, BigDecimal amount) {
        if (fromCurrency.equals(toCurrency)) {
            throw new IllegalArgumentException("Cannot exchange identical currencies");
        }
        Balance fromBalance = this.balanceRepository.getBalanceByAccountIdAndCurrency(accountId, fromCurrency)
                .orElseThrow(() -> new ResourceNotFoundException("No balance found in " + fromCurrency));
        if (fromBalance.getAmount().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Amount is greater than in existing Account");
        }
        BigDecimal rate = this.currencyConversionService.getRate(fromCurrency, toCurrency);
        BigDecimal convertedAmount = amount.multiply(rate);
        Balance toBalance = this.balanceRepository.getBalanceByAccountIdAndCurrency(accountId, toCurrency)
                .orElseGet(() -> new Balance(null, accountId, toCurrency, BigDecimal.ZERO));
        fromBalance.setAmount(fromBalance.getAmount().subtract(amount));
        toBalance.setAmount(toBalance.getAmount().add(convertedAmount));
        this.balanceRepository.saveAll(List.of(fromBalance, toBalance));
        return this.balanceMapper.toBalanceDto(toBalance);
    }

}
