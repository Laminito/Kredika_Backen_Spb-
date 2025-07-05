package sn.kredika_app.domain.dto.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductRequestDto {
    @NotBlank
    @Size(min = 2, max = 255)
    private String name;

    private String description;
    private String shortDescription;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal price;

    private BigDecimal comparePrice;
    private String sku;
    private Integer stock;
    private UUID categoryId;
    private Boolean creditEligible;
    private BigDecimal minCreditAmount;
    private Integer maxCreditDuration;
}