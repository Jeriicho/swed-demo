package ee.swedbank.balance.service;

import ee.swedbank.balance.model.CurrencyType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Component
public class CurrencyConversionService {

    private final Map<String, BigDecimal> exchangeRates = Map.ofEntries(
            Map.entry("EUR_TO_USD", BigDecimal.valueOf(1.14)),
            Map.entry("EUR_TO_SEK", BigDecimal.valueOf(10.82)),
            Map.entry("EUR_TO_RUB", BigDecimal.valueOf(90.2)),
            Map.entry("USD_TO_EUR", BigDecimal.valueOf(0.88)),
            Map.entry("USD_TO_SEK", BigDecimal.valueOf(9.53)),
            Map.entry("USD_TO_RUB", BigDecimal.valueOf(79.34)),
            Map.entry("SEK_TO_EUR", BigDecimal.valueOf(0.092)),
            Map.entry("SEK_TO_USD", BigDecimal.valueOf(0.1)),
            Map.entry("SEK_TO_RUB", BigDecimal.valueOf(8.32)),
            Map.entry("RUB_TO_EUR", BigDecimal.valueOf(0.011)),
            Map.entry("RUB_TO_USD", BigDecimal.valueOf(0.013)),
            Map.entry("RUB_TO_SEK", BigDecimal.valueOf(0.12))
    );

    public BigDecimal getRate(CurrencyType from, CurrencyType to) {
        return Optional.ofNullable(exchangeRates.get(from.name() + "_TO_" + to.name()))
                .orElseThrow(() -> new IllegalArgumentException("Unsupported currency pair"));
    }
}

