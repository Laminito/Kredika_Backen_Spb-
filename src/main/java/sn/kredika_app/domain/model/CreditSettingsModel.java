package sn.kredika_app.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Représente les paramètres de crédit configurés dans le système. Ces paramètres déterminent les conditions d'octroi de
 * crédit aux clients.
 */
@Entity
@Table(name = "credit_settings", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreditSettingsModel extends BaseModel {

    /**
     * Durée maximale du crédit en mois Doit être comprise entre 1 et 36 mois
     */
    @NotNull(message = "La durée en mois est obligatoire")
    @Min(value = 1, message = "La durée minimale est de 1 mois")
    @Max(value = 36, message = "La durée maximale est de 36 mois")
    @Column(name = "duration_months", nullable = false)
    private Integer durationMonths;

    /**
     * Taux de commission appliqué aux crédits (entre 0 et 1) Ex: 0.05 pour 5% de commission
     */
    @NotNull(message = "Le taux de commission est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le taux doit être strictement positif")
    @DecimalMax(value = "1.0", message = "Le taux ne peut dépasser 100%")
    @Digits(integer = 1, fraction = 4, message = "Format invalide (max 4 décimales)")
    @Column(name = "commission_rate", precision = 5, scale = 4, nullable = false)
    private BigDecimal commissionRate;

    /**
     * Montant minimum autorisé pour un crédit
     * Doit être positif si renseigné
     */
    @DecimalMin(value = "0.0", inclusive = false, message = "Le montant minimum doit être positif")
    @Digits(integer = 10, fraction = 2, message = "Format invalide (10 chiffres avant, 2 après la virgule)")
    @Column(name = "min_amount", precision = 10, scale = 2)
    private BigDecimal minAmount;

    /**
     * Montant maximum autorisé pour un crédit
     * Doit être supérieur au montant minimum si renseigné
     */
    @DecimalMin(value = "0.0", inclusive = false, message = "Le montant maximum doit être positif")
    @Digits(integer = 10, fraction = 2, message = "Format invalide (10 chiffres avant, 2 après la virgule)")
    @Column(name = "max_amount", precision = 10, scale = 2)
    private BigDecimal maxAmount;

    /**
     * Indique si ces paramètres sont actifs et utilisables
     * Valeur par défaut: true
     */
    @Column(name = "is_active")
    private Boolean isActive = true;

    /**
     * Description des conditions de crédit
     * Peut contenir des informations complémentaires
     */
    @Size(max = 500, message = "La description ne peut excéder 500 caractères")
    @Column(name = "description", length = 500)
    private String description;

    /**
     * Vérifie si un montant est dans la plage autorisée
     *
     * @param amount Montant à vérifier
     * @return true si le montant est valide, false sinon
     */
    public boolean isAmountValid (BigDecimal amount) {
        if (amount == null) return false;

        boolean valid = true;
        if (minAmount != null) {
            valid = amount.compareTo(minAmount) >= 0;
        }
        if (maxAmount != null) {
            valid = valid && amount.compareTo(maxAmount) <= 0;
        }
        return valid;
    }

    /**
     * Calcule le montant total avec commission
     *
     * @param principal Montant principal du crédit
     * @return Montant total à rembourser (principal + commission)
     */
    public BigDecimal calculateTotalAmount (BigDecimal principal) {
        if (principal == null) return null;
        return principal.add(principal.multiply(commissionRate));
    }

    @NotNull(message = "La durée est requise")
    public Integer getDurationMonths () {
        return durationMonths;
    }

    public void setDurationMonths (@NotNull(message = "La durée est requise") Integer durationMonths) {
        this.durationMonths = durationMonths;
    }

    @NotNull(message = "Le taux de commission est requis")
    @DecimalMin(value = "0.0", message = "Le taux de commission doit être positif")
    public BigDecimal getCommissionRate () {
        return commissionRate;
    }

    public void setCommissionRate (
            @NotNull(message = "Le taux de commission est requis")
            @DecimalMin(value = "0.0", message = "Le taux de commission doit être positif")
            BigDecimal commissionRate
    ) {
        this.commissionRate = commissionRate;
    }
    public BigDecimal getMinAmount () {
        return minAmount;
    }

    public void setMinAmount (BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public BigDecimal getMaxAmount () {
        return maxAmount;
    }

    public void setMaxAmount (BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Boolean getActive () {
        return isActive;
    }

    public void setActive (Boolean active) {
        isActive = active;
    }

    public String getDescription () {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }
}