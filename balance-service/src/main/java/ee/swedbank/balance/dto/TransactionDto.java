package ee.swedbank.balance.dto;

import ee.swedbank.balance.model.CurrencyType;
import ee.swedbank.balance.model.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TransactionDto {

    private Integer accountIdentifier;
    private String accountName;
    private TransactionType type;
    private CurrencyType currency;
    private BigDecimal amount;
    private BigDecimal balance;
    private LocalDateTime timestamp;

}
