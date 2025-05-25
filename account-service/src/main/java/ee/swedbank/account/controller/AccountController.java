package ee.swedbank.account.controller;

import ee.swedbank.account.service.AccountService;
import ee.swedbank.common.dto.AccountDto;
import ee.swedbank.common.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody AccountDto getAccountById(@PathVariable long id) {
        return accountService.getAccountById(id);
    }

}
