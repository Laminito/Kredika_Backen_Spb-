package sn.kredika_app.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;


/**
 * Représente le profil de crédit d'un utilisateur, contenant toutes les informations relatives à sa solvabilité et ses
 * limites de crédit.
 */
@Entity
@Table(name = "credit_profiles", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreditProfileModel extends BaseModel {

    /**
     * Identifiant de l'utilisateur associé à ce profil de crédit Clé étrangère vers UserModel
     */
    @Column(name = "user_id")
    private UUID userId;

    /**
     * Score de crédit de l'utilisateur (entre 300 et 850) Plus le score est élevé, meilleure est la solvabilité
     */
    @Min(value = 300, message = "Le score de crédit minimum est 300")
    @Max(value = 850, message = "Le score de crédit maximum est 850")
    @Column(name = "credit_score")
    private Integer creditScore;

    /**
     * Limite de crédit totale autorisée pour l'utilisateur
     * Doit être positive ou nulle
     */
    @NotNull(message = "La limite de crédit est obligatoire")
    @DecimalMin(value = "0.00", message = "La limite de crédit ne peut être négative")
    @Digits(integer = 10, fraction = 2, message = "Format invalide (10 chiffres avant, 2 après la virgule)")
    @Column(name = "credit_limit", precision = 10, scale = 2, nullable = false)
    private BigDecimal creditLimit;

    /**
     * Crédit disponible (limite de crédit moins dette totale)
     * Doit être positif ou nul et inférieur à la limite de crédit
     */
    @NotNull(message = "Le crédit disponible est obligatoire")
    @DecimalMin(value = "0.00", message = "Le crédit disponible ne peut être négatif")
    @Digits(integer = 10, fraction = 2, message = "Format invalide (10 chiffres avant, 2 après la virgule)")
    @Column(name = "available_credit", precision = 10, scale = 2, nullable = false)
    private BigDecimal availableCredit;

    /**
     * Dette totale actuelle de l'utilisateur
     * Valeur par défaut: 0.00
     */
    @NotNull(message = "La dette totale est obligatoire")
    @DecimalMin(value = "0.00", message = "La dette totale ne peut être négative")
    @Digits(integer = 10, fraction = 2, message = "Format invalide (10 chiffres avant, 2 après la virgule)")
    @Column(name = "total_debt", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalDebt = BigDecimal.ZERO;

    /**
     * Nombre de défauts de paiement enregistrés
     * Utilisé pour calculer le score de crédit
     */
    @Min(value = 0, message = "Le nombre de défauts ne peut être négatif")
    @Column(name = "default_count")
    private Integer defaultCount = 0;

    /**
     * Date de la dernière révision du crédit
     * Doit être dans le passé si renseignée
     */
    @PastOrPresent(message = "La date de révision doit être dans le présent ou le passé")
    @Column(name = "last_credit_review")
    private LocalDate lastCreditReview;

    // ===== RELATIONS =====

    /**
     * Référence vers l'utilisateur associé à ce profil Relation One-to-One bidirectionnelle avec UserModel Ne fait pas
     * partie de la sérialisation JSON
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnore
    private UserModel user;

    /**
     * Calcule le ratio d'utilisation du crédit (dette totale / limite de crédit)
     *
     * @return Le ratio entre 0 et 1, ou null si la limite de crédit est nulle
     */
    public BigDecimal getCreditUtilizationRatio () {
        if (creditLimit.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        return totalDebt.divide(creditLimit, 4, RoundingMode.HALF_UP);
    }

    /**
     * Vérifie si le crédit est épuisé
     *
     * @return true si le crédit disponible <= 0, false sinon
     */
    public boolean isCreditExhausted () {
        return availableCredit.compareTo(BigDecimal.ZERO) <= 0;
    }

    /**
     * Vérifie si l'utilisateur est en défaut (trop de retards de paiement)
     *
     * @return true si defaultCount >= 3, false sinon
     */
    public boolean isInDefault () {
        return defaultCount != null && defaultCount >= 3;
    }

    public UUID getUserId () {
        return userId;
    }

    public void setUserId (UUID userId) {
        this.userId = userId;
    }

    public Integer getCreditScore () {
        return creditScore;
    }

    public void setCreditScore (Integer creditScore) {
        this.creditScore = creditScore;
    }

    public @NotNull(message = "Credit limit is required") BigDecimal getCreditLimit () {
        return creditLimit;
    }

    public void setCreditLimit (@NotNull(message = "Credit limit is required") BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public @NotNull(message = "Available credit is required") BigDecimal getAvailableCredit () {
        return availableCredit;
    }

    public void setAvailableCredit (@NotNull(message = "Available credit is required") BigDecimal availableCredit) {
        this.availableCredit = availableCredit;
    }

    public @NotNull(message = "Total debt is required") BigDecimal getTotalDebt () {
        return totalDebt;
    }

    public void setTotalDebt (@NotNull(message = "Total debt is required") BigDecimal totalDebt) {
        this.totalDebt = totalDebt;
    }

    public Integer getDefaultCount () {
        return defaultCount;
    }

    public void setDefaultCount (Integer defaultCount) {
        this.defaultCount = defaultCount;
    }

    public LocalDate getLastCreditReview () {
        return lastCreditReview;
    }

    public void setLastCreditReview (LocalDate lastCreditReview) {
        this.lastCreditReview = lastCreditReview;
    }

    public UserModel getUser () {
        return user;
    }

    public void setUser (UserModel user) {
        this.user = user;
    }
}