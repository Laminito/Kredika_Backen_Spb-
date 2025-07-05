package sn.kredika_app.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequestDto {
    @NotBlank
    @Size(min = 2, max = 100)
    private String fullName;

    @NotBlank
    @Email
    private String email;

    @Pattern(regexp = "^\\+(?:[0-9] ?){6,14}[0-9]$")
    private String phoneNumber;

    @Past
    private LocalDate dateOfBirth;

    @Size(min = 5, max = 50)
    private String nationalId;

    @Size(max = 100)
    private String profession;

    @DecimalMin("0.0")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal monthlyIncome;

    @URL
    private String profileImageUrl;
}