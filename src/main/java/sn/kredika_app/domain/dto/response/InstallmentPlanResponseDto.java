package sn.kredika_app.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sn.kredika_app.domain.dto.simple.OrderSimpleDto;
import sn.kredika_app.domain.dto.simple.ProductSimpleDto;
import sn.kredika_app.domain.dto.simple.UserSimpleDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InstallmentPlanResponseDto {
    private UUID id;
    private String planNumber;
    private UserSimpleDto user;
    private OrderSimpleDto order;
    private ProductSimpleDto product;
    private BigDecimal principalAmount;
    private BigDecimal totalAmount;
    private BigDecimal installmentAmount;
    private Integer durationMonths;
    private String statusCode;
    private LocalDate startDate;
    private LocalDate endDate;
}
