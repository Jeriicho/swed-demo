package ee.swedbank.balance.service;

import ee.swedbank.balance.mapper.TransactionMapper;
import ee.swedbank.balance.client.AccountClient;
import ee.swedbank.balance.dto.TransactionDto;
import ee.swedbank.balance.model.CurrencyType;
import ee.swedbank.balance.model.Transaction;
import ee.swedbank.balance.model.TransactionType;
import ee.swedbank.balance.repository.TransactionRepository;
import ee.swedbank.common.dto.AccountDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final AccountClient accountClient;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionService(AccountClient accountClient,
                              TransactionRepository transactionRepository,
                              TransactionMapper transactionMapper) {
        this.accountClient = accountClient;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    public void recordTransaction(Long accountId,
                                  TransactionType transactionType,
                                  CurrencyType currencyType,
                                  BigDecimal balance,
                                  BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setType(transactionType);
        transaction.setBalance(balance);
        transaction.setAmount(amount);
        transaction.setCurrency(currencyType);
        transactionRepository.save(transaction);
    }

    public List<TransactionDto> findTransactionsByAccountId(long accountId) {
        List<Transaction> transactions = this.transactionRepository.findByAccountId(accountId);
        if (!transactions.isEmpty()) {
            AccountDto accountDto = accountClient.getAccountById(accountId);
            return transactions.stream()
                    .map(transaction -> this.transactionMapper.toDto(transaction, accountDto.getName(), accountDto.getIdentificationCode()))
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}
