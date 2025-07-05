package sn.kredika_app.domain.dto.persistence;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DimensionsPersistenceDto {
    private BigDecimal length;  // en cm
    private BigDecimal width;
    private BigDecimal height;
    private String unit;        // "cm", "m", "inch"
}
