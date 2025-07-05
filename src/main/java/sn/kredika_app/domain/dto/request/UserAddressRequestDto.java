package sn.kredika_app.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAddressRequestDto {
    private String typeCode;

    @NotBlank
    private String street;

    @NotBlank
    private String city;

    @NotBlank
    private String country;

    private String region;
    private String postalCode;
    private Boolean isDefault;

    @DecimalMin("-90.0") @DecimalMax("90.0")
    private Double latitude;

    @DecimalMin("-180.0") @DecimalMax("180.0")
    private Double longitude;
}
