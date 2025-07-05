package sn.kredika_app.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditProfileResponseDto {
    private Integer creditScore;
    private BigDecimal creditLimit;
    private BigDecimal availableCredit;
    private BigDecimal totalDebt;
    private LocalDate lastCreditReview;
}
