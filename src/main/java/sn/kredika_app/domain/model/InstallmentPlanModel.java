package sn.kredika_app.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Représente un plan de paiement échelonné pour un achat à crédit. Contient tous les détails du crédit et le calendrier
 * de remboursement.
 */
@Entity
@Table(name = "installment_plans", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InstallmentPlanModel extends BaseModel {

    /**
     * Numéro unique identifiant le plan de paiement Format: "PLAN-YYYYMMDD-XXXXX"
     */
    @Pattern(regexp = "^PLAN-\\d{8}-\\d{5}$", message = "Le format du numéro de plan est invalide")
    @Column(name = "plan_number", unique = true, length = 20)
    private String planNumber;

    /**
     * Identifiant de l'utilisateur bénéficiaire du crédit
     */
    @NotNull(message = "L'identifiant utilisateur est obligatoire")
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    /**
     * Identifiant de la commande associée
     */
    @Column(name = "order_id")
    private UUID orderId;

    /**
     * Identifiant du produit financé (si applicable)
     */
    @Column(name = "product_id")
    private UUID productId;

    /**
     * Montant principal du crédit (hors commissions)
     * Doit être strictement positif
     */
    @NotNull(message = "Le montant principal est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant principal doit être positif")
    @Digits(integer = 10, fraction = 2, message = "Format invalide (10 chiffres avant, 2 après la virgule)")
    @Column(name = "principal_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal principalAmount;

    /**
     * Taux de commission appliqué (entre 0 et 1)
     * Ex: 0.05 pour 5% de commission
     */
    @NotNull(message = "Le taux de commission est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le taux doit être strictement positif")
    @DecimalMax(value = "1.0", message = "Le taux ne peut dépasser 100%")
    @Digits(integer = 1, fraction = 4, message = "Format invalide (max 4 décimales)")
    @Column(name = "commission_rate", precision = 5, scale = 4, nullable = false)
    private BigDecimal commissionRate;

    /**
     * Montant total des commissions
     * Calculé automatiquement: principalAmount * commissionRate
     */
    @NotNull(message = "Le montant de commission est obligatoire")
    @DecimalMin(value = "0.0", message = "Le montant de commission ne peut être négatif")
    @Digits(integer = 10, fraction = 2, message = "Format invalide (10 chiffres avant, 2 après la virgule)")
    @Column(name = "commission_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal commissionAmount;

    /**
     * Montant total à rembourser (principal + commissions)
     * Calculé automatiquement: principalAmount + commissionAmount
     */
    @NotNull(message = "Le montant total est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant total doit être positif")
    @Digits(integer = 10, fraction = 2, message = "Format invalide (10 chiffres avant, 2 après la virgule)")
    @Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    /**
     * Montant de chaque échéance
     * Calculé automatiquement: totalAmount / nombre d'échéances
     */
    @NotNull(message = "Le montant de l'échéance est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant de l'échéance doit être positif")
    @Digits(integer = 10, fraction = 2, message = "Format invalide (10 chiffres avant, 2 après la virgule)")
    @Column(name = "installment_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal installmentAmount;

    /**
     * Durée totale du crédit en mois (1 à 36)
     */
    @Min(value = 1, message = "La durée minimale est de 1 mois")
    @Max(value = 36, message = "La durée maximale est de 36 mois")
    @Column(name = "duration_months")
    private Integer durationMonths;

    /**
     * Fréquence de paiement (MENSUEL, TRIMESTRIEL, etc.)
     * Doit correspondre à un code existant dans le système
     */
    @Size(max = 20, message = "Le code de fréquence ne peut excéder 20 caractères")
    @Column(name = "frequency_code", length = 20)
    private String frequencyCode;

    /**
     * Nombre total d'échéances
     */
    @Min(value = 1, message = "Il doit y avoir au moins une échéance")
    @Column(name = "total_installments")
    private Integer totalInstallments;

    /**
     * Nombre d'échéances déjà payées
     */
    @Min(value = 0, message = "Le nombre d'échéances payées ne peut être négatif")
    @Column(name = "paid_installments")
    private Integer paidInstallments = 0;

    /**
     * Date de début du plan de paiement
     * Doit être dans le futur ou aujourd'hui
     */
    @FutureOrPresent(message = "La date de début doit être aujourd'hui ou dans le futur")
    @Column(name = "start_date")
    private LocalDate startDate;

    /**
     * Date de fin théorique du plan
     * Calculée automatiquement: startDate + durationMonths
     */
    @Future(message = "La date de fin doit être dans le futur")
    @Column(name = "end_date")
    private LocalDate endDate;

    /**
     * Statut courant du plan (ACTIF, EN_RETARD, TERMINE, etc.)
     * Doit correspondre à un code existant dans le système
     */
    @Size(max = 20, message = "Le code statut ne peut excéder 20 caractères")
    @Column(name = "status_code", length = 20)
    private String statusCode;

    /**
     * Montant des pénalités pour retard de paiement
     * Valeur par défaut: 0
     */
    @DecimalMin(value = "0.0", message = "Le montant des pénalités ne peut être négatif")
    @Digits(integer = 10, fraction = 2, message = "Format invalide (10 chiffres avant, 2 après la virgule)")
    @Column(name = "late_penalty", precision = 10, scale = 2)
    private BigDecimal latePenalty = BigDecimal.ZERO;

    /**
     * Date effective de complétion du plan
     * Null si le plan est encore en cours
     */
    @PastOrPresent(message = "La date de complétion doit être dans le passé ou aujourd'hui")
    @Column(name = "completed_at")
    private LocalDateTime completedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnore
    private UserModel user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    @JsonIgnore
    private OrderModel order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    @JsonIgnore
    private ProductModel product;

    @OneToMany(mappedBy = "installmentPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private List<PaymentScheduleModel> paymentSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "installmentPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private List<PaymentTransactionModel> paymentTransactions = new ArrayList<>();

    /**
     * Calcule le montant restant à payer
     *
     * @return totalAmount - (installmentAmount * paidInstallments)
     */
    public BigDecimal getRemainingAmount () {
        return totalAmount.subtract(
                installmentAmount.multiply(BigDecimal.valueOf(paidInstallments))
        );
    }

    /**
     * Vérifie si le plan est complètement payé
     *
     * @return true si paidInstallments >= totalInstallments, false sinon
     */
    public boolean isFullyPaid () {
        return paidInstallments >= totalInstallments;
    }

    /**
     * Vérifie si le plan est en retard de paiement
     *
     * @return true si la date actuelle est après endDate et pas complètement payé
     */
    public boolean isLate () {
        return !isFullyPaid() && LocalDate.now().isAfter(endDate);
    }


    public String getPlanNumber () {
        return planNumber;
    }

    public void setPlanNumber (String planNumber) {
        this.planNumber = planNumber;
    }

    public UUID getUserId () {
        return userId;
    }

    public void setUserId (UUID userId) {
        this.userId = userId;
    }

    public UUID getOrderId () {
        return orderId;
    }

    public void setOrderId (UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getProductId () {
        return productId;
    }

    public void setProductId (UUID productId) {
        this.productId = productId;
    }

    public @NotNull(message = "Principal amount is required") BigDecimal getPrincipalAmount () {
        return principalAmount;
    }

    public void setPrincipalAmount (@NotNull(message = "Principal amount is required") BigDecimal principalAmount) {
        this.principalAmount = principalAmount;
    }

    public @NotNull(message = "Commission rate is required") BigDecimal getCommissionRate () {
        return commissionRate;
    }

    public void setCommissionRate (@NotNull(message = "Commission rate is required") BigDecimal commissionRate) {
        this.commissionRate = commissionRate;
    }

    public @NotNull(message = "Commission amount is required") BigDecimal getCommissionAmount () {
        return commissionAmount;
    }

    public void setCommissionAmount (@NotNull(message = "Commission amount is required") BigDecimal commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public @NotNull(message = "Total amount is required") BigDecimal getTotalAmount () {
        return totalAmount;
    }

    public void setTotalAmount (@NotNull(message = "Total amount is required") BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public @NotNull(message = "Installment amount is required") BigDecimal getInstallmentAmount () {
        return installmentAmount;
    }

    public void setInstallmentAmount (@NotNull(message = "Installment amount is required") BigDecimal installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public Integer getDurationMonths () {
        return durationMonths;
    }

    public void setDurationMonths (Integer durationMonths) {
        this.durationMonths = durationMonths;
    }

    public String getFrequencyCode () {
        return frequencyCode;
    }

    public void setFrequencyCode (String frequencyCode) {
        this.frequencyCode = frequencyCode;
    }

    public Integer getTotalInstallments () {
        return totalInstallments;
    }

    public void setTotalInstallments (Integer totalInstallments) {
        this.totalInstallments = totalInstallments;
    }

    public Integer getPaidInstallments () {
        return paidInstallments;
    }

    public void setPaidInstallments (Integer paidInstallments) {
        this.paidInstallments = paidInstallments;
    }

    public LocalDate getStartDate () {
        return startDate;
    }

    public void setStartDate (LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate () {
        return endDate;
    }

    public void setEndDate (LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatusCode () {
        return statusCode;
    }

    public void setStatusCode (String statusCode) {
        this.statusCode = statusCode;
    }

    public BigDecimal getLatePenalty () {
        return latePenalty;
    }

    public void setLatePenalty (BigDecimal latePenalty) {
        this.latePenalty = latePenalty;
    }

    public LocalDateTime getCompletedAt () {
        return completedAt;
    }

    public void setCompletedAt (LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public UserModel getUser () {
        return user;
    }

    public void setUser (UserModel user) {
        this.user = user;
    }

    public OrderModel getOrder () {
        return order;
    }

    public void setOrder (OrderModel order) {
        this.order = order;
    }

    public ProductModel getProduct () {
        return product;
    }

    public void setProduct (ProductModel product) {
        this.product = product;
    }

    public List<PaymentScheduleModel> getPaymentSchedules () {
        return paymentSchedules;
    }

    public void setPaymentSchedules (List<PaymentScheduleModel> paymentSchedules) {
        this.paymentSchedules = paymentSchedules;
    }

    public List<PaymentTransactionModel> getPaymentTransactions () {
        return paymentTransactions;
    }

    public void setPaymentTransactions (List<PaymentTransactionModel> paymentTransactions) {
        this.paymentTransactions = paymentTransactions;
    }
}