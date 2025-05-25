package ee.swedbank.balance.client;

import ee.swedbank.common.dto.AccountDto;
import ee.swedbank.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class AccountClient {

    private final WebClient webClient;

    public AccountClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public AccountDto getAccountById(long accountId) {
        try {
            return webClient.get()
                    .uri("/account/{id}", accountId)
                    .retrieve()
                    .bodyToMono(AccountDto.class)
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            throw new ResourceNotFoundException("No account with id " + accountId + " found");
        }
    }
}

