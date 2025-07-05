package sn.kredika_app.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sn.kredika_app.domain.dto.simple.CategorySimpleDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponseDto {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal comparePrice;
    private String sku;
    private Integer stock;
    private String mainImageUrl;
    private BigDecimal rating;
    private Integer reviewCount;
    private Boolean creditEligible;
    private CategorySimpleDto category;
    private List<ProductImageResponseDto> images;
}
