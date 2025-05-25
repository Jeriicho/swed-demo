package ee.swedbank.balance.dto;

import ee.swedbank.balance.model.CurrencyType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class CurrencyExchangeRequest {

    @NotNull
    private CurrencyType fromCurrency;

    @NotNull
    private CurrencyType toCurrency;

    @NotNull
    @Min(value = 1, message = "Amount must be greater than 1")
    private BigDecimal amount;

}
