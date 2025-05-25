package ee.swedbank.balance.mapper;

import ee.swedbank.balance.dto.TransactionDto;
import ee.swedbank.balance.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "accountName", source = "accountName")
    @Mapping(target = "accountIdentifier", source = "accountIdentifier")
    TransactionDto toDto(Transaction transaction, String accountName, Integer accountIdentifier);

}
