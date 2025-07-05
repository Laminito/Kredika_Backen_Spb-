package sn.kredika_app.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GatewayResponseDto {
    private String transactionId;
    private String status;           // "success", "failed"
    private BigDecimal amount;
    private String currency;          // "EUR", "USD"
    private ZonedDateTime timestamp;
    private GatewayErrorDto error;     // Si échec

    public static class GatewayErrorDto {
        private String code;
        private String message;
    }
}