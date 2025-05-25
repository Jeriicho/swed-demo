package ee.swedbank.account.mapper;

import ee.swedbank.common.dto.AccountDto;
import ee.swedbank.account.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDto toAccount(Account destination);

}
