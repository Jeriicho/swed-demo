package ee.swedbank.account.service;

import ee.swedbank.account.mapper.AccountMapper;
import ee.swedbank.account.model.Account;
import ee.swedbank.account.repository.AccountRepository;
import ee.swedbank.common.dto.AccountDto;
import ee.swedbank.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    public AccountDto getAccountById(Long id) {
        Account account = this.accountRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No account with id " + id + " found")
        );
        return this.accountMapper.toAccount(account);
    }

}
