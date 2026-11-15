package ng.safebanking.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountResponse(
        Long id,
        String accountNumber,
        String accountName,
        BigDecimal balance,
        LocalDateTime createdAt

){
}
