package sn.kredika_app.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

/**
 * DTO représentant la liste complète de souhaits d'un utilisateur
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WishlistResponseDto {
    private UUID userId;
    private String userName;
    private List<WishlistItemResponseDto> items;
    private int totalItems;
}
