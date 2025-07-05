package sn.kredika_app.domain.dto.persistence;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationDataPersistenceDto {
    private String orderId;           // Pour les notifications de commande
    private BigDecimal amount;        // Montant si pertinent
    private String reference;         // Référence métier
    private String deepLink;          // Lien profond pour mobile/app
    private ZonedDateTime expiryDate; // Pour les notifications expirables
}
