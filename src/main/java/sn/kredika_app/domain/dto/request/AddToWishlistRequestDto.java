package sn.kredika_app.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

/**
 * DTO pour ajouter un produit à la liste de souhaits
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddToWishlistRequestDto {
    @NotNull(message = "L'ID du produit est requis")
    private UUID productId;

    @Size(max = 500, message = "Les notes ne peuvent excéder 500 caractères")
    private String notes;

    @Min(value = 1, message = "La priorité minimale est 1")
    @Max(value = 5, message = "La priorité maximale est 5")
    private Integer priority = 1;

}
