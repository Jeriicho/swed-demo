package ee.swedbank.balance.dto;

import ee.swedbank.balance.model.CurrencyType;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BalanceDto {

    @NotNull(message = "Currency cannot be null")
    private CurrencyType currency;

    @DecimalMin(value = "0.1", message = "Amount must be greater than zero")
    @NotNull(message = "Amount cannot be null")
    private BigDecimal amount;

}
