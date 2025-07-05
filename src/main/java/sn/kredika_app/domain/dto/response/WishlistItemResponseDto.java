package sn.kredika_app.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO représentant un élément de la liste de souhaits
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WishlistItemResponseDto {
    private UUID id;
    private UUID productId;
    private String productName;
    private String productImageUrl;
    private BigDecimal productPrice;
    private String notes;
    private Integer priority;
    private LocalDateTime addedAt;
}
