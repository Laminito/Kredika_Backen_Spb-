package sn.kredika_app.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAddressResponseDto {
    private UUID id;
    private String typeCode;
    private String street;
    private String city;
    private String region;
    private String postalCode;
    private String country;
    private Boolean isDefault;
    private String fullAddress;

    private Double latitude;
    private Double longitude;

    public String getFullAddress () {
        return String.format("%s, %s, %s %s", street, city, postalCode, country);
    }
}
