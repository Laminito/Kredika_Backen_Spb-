package sn.kredika_app.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sn.kredika_app.domain.dto.simple.ProductSimpleDto;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItemResponseDto {
    private UUID id;
    private ProductSimpleDto product;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private String paymentMethodCode;
    private Integer creditDuration;
    private String creditFrequencyCode;
    private BigDecimal installmentAmount;
}
