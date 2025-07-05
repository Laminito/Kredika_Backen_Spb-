package sn.kredika_app.domain.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CartResponseDto {
    private UUID id;
    private String statusCode;
    private BigDecimal totalAmount;
    private List<CartItemResponseDto> items;
    private LocalDateTime expiresAt;
}
