package sn.kredika_app.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "credit_settings", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreditSettingsModel extends BaseModel {
    @NotNull(message = "La durée est requise")
    @Column(name = "duration_months", nullable = false)
    private Integer durationMonths;

    @NotNull(message = "Le taux de commission est requis")
    @DecimalMin(value = "0.0", message = "Le taux de commission doit être positif")
    @Column(name = "commission_rate", precision = 5, scale = 4, nullable = false)
    private BigDecimal commissionRate;

    @Column(name = "min_amount", precision = 10, scale = 2)
    private BigDecimal minAmount;

    @Column(name = "max_amount", precision = 10, scale = 2)
    private BigDecimal maxAmount;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "description")
    private String description;

    public @NotNull(message = "La durée est requise") Integer getDurationMonths () {
        return durationMonths;
    }

    public void setDurationMonths (@NotNull(message = "La durée est requise") Integer durationMonths) {
        this.durationMonths = durationMonths;
    }

    public @NotNull(message = "Le taux de commission est requis") @DecimalMin(value = "0.0", message = "Le taux de commission doit être positif") BigDecimal getCommissionRate () {
        return commissionRate;
    }

    public void setCommissionRate (@NotNull(message = "Le taux de commission est requis") @DecimalMin(value = "0.0", message = "Le taux de commission doit être positif") BigDecimal commissionRate) {
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