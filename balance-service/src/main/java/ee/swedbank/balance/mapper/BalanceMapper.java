package ee.swedbank.balance.mapper;

import ee.swedbank.balance.dto.BalanceDto;
import ee.swedbank.balance.model.Balance;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BalanceMapper {

    List<BalanceDto> toBalanceDtos(List<Balance> balance);

    BalanceDto toBalanceDto(Balance balance);

}
