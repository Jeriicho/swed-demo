package ee.swedbank.balance.controller;

import ee.swedbank.balance.dto.TransactionDto;
import ee.swedbank.balance.service.TransactionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/account/{accountId}/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public List<TransactionDto> getTransactions(@PathVariable("accountId") long accountId) {
        return transactionService.findTransactionsByAccountId(accountId);
    }

}
