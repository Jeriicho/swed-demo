package ee.swedbank.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountDto {

    private Long id;
    private Integer identificationCode;
    private String name;

}
