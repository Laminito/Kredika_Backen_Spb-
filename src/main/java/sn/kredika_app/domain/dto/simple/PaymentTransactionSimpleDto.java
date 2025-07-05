package sn.kredika_app.domain.dto.simple;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentTransactionSimpleDto {
    private UUID id;
    private String transactionNumber;
    private BigDecimal amount;
    private String paymentMethodCode;
    private LocalDateTime processedAt;
}
