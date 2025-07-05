package sn.kredika_app.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sn.kredika_app.domain.dto.simple.UserSimpleDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponseDto {
    private UUID id;
    private String orderNumber;
    private UserSimpleDto user;
    private UserAddressResponseDto deliveryAddress;
    private BigDecimal totalAmount;
    private String statusCode;
    private LocalDateTime createdAt;
    private List<OrderItemResponseDto> items;
}
