package sn.kredika_app.domain.dto.response.land;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LandResponseDto {
    private UUID id;
    private String location;
    private String landType;
    private UUID ownerId;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private List<LandDocumentResponseDto> documents;
}
