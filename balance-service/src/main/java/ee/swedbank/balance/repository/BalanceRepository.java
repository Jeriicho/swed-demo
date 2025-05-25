package ee.swedbank.balance.repository;

import ee.swedbank.balance.model.Balance;
import ee.swedbank.balance.model.CurrencyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {

    List<Balance> findBalancesByAccountId(Long id);

    Optional<Balance> getBalanceByAccountIdAndCurrency(Long id, CurrencyType currency);

}
