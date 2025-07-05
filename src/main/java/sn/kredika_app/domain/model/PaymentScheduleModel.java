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
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Représente une échéance de paiement dans un plan de paiement échelonné. Contient les informations spécifiques à
 * chaque tranche de remboursement.
 */
@Entity
@Table(name = "payment_schedules", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PaymentScheduleModel extends BaseModel {

    /**
     * Identifiant du plan de paiement échelonné parent Clé étrangère vers InstallmentPlanModel
     */
    @NotNull(message = "L'identifiant du plan de paiement est obligatoire")
    @Column(name = "installment_plan_id", nullable = false)
    private UUID installmentPlanId;

    /**
     * Numéro de l'échéance dans le plan (1 à N)
     */
    @NotNull(message = "Le numéro d'échéance est obligatoire")
    @Min(value = 1, message = "Le numéro d'échéance doit être au moins 1")
    @Column(name = "installment_number", nullable = false)
    private Integer installmentNumber;

    /**
     * Date d'échéance du paiement Doit être dans le futur lors de la création
     */
    @FutureOrPresent(message = "La date d'échéance doit être aujourd'hui ou dans le futur")
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    /**
     * Montant total dû pour cette échéance (capital + intérêts)
     * Doit être strictement positif
     */
    @NotNull(message = "Le montant dû est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant dû doit être positif")
    @Digits(integer = 10, fraction = 2, message = "Format de montant invalide (10 chiffres avant, 2 après la virgule)")
    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    /**
     * Montant déjà payé pour cette échéance
     * Valeur par défaut: 0.00
     */
    @NotNull(message = "Le montant payé est obligatoire")
    @DecimalMin(value = "0.00", message = "Le montant payé ne peut être négatif")
    @Digits(integer = 10, fraction = 2, message = "Format de montant invalide (10 chiffres avant, 2 après la virgule)")
    @Column(name = "paid_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    /**
     * Montant des pénalités appliquées (retard de paiement)
     * Valeur par défaut: 0.00
     */
    @NotNull(message = "Le montant des pénalités est obligatoire")
    @DecimalMin(value = "0.00", message = "Le montant des pénalités ne peut être négatif")
    @Digits(integer = 10, fraction = 2, message = "Format de montant invalide (10 chiffres avant, 2 après la virgule)")
    @Column(name = "penalty_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal penaltyAmount = BigDecimal.ZERO;

    /**
     * Date et heure effective du paiement
     * Null si l'échéance n'a pas encore été payée
     */
    @PastOrPresent(message = "La date de paiement doit être dans le passé ou présent")
    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    /**
     * Référence vers le plan de paiement parent Relation Many-to-One vers InstallmentPlanModel Ne fait pas partie de la
     * sérialisation JSON
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "installment_plan_id", insertable = false, updatable = false)
    @JsonIgnore
    private InstallmentPlanModel installmentPlan;

    /**
     * Liste des transactions de paiement associées à cette échéance
     * Relation One-to-Many vers PaymentTransactionModel
     */
    @OneToMany(
            mappedBy = "paymentSchedule",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<PaymentTransactionModel> paymentTransactions = new ArrayList<>();

    // ===== METHODES UTILITAIRES =====

    /**
     * Calcule le montant restant à payer pour cette échéance
     *
     * @return amount - paidAmount
     */
    public BigDecimal getRemainingAmount () {
        return amount.subtract(paidAmount);
    }

    /**
     * Vérifie si l'échéance est complètement payée
     *
     * @return true si paidAmount >= amount, false sinon
     */
    public boolean isFullyPaid () {
        return paidAmount.compareTo(amount) >= 0;
    }

    /**
     * Vérifie si l'échéance est en retard
     *
     * @return true si dueDate est passée et pas complètement payée, false sinon
     */
    public boolean isOverdue () {
        return !isFullyPaid() && dueDate.isBefore(LocalDate.now());
    }

    /**
     * Marque l'échéance comme payée avec la date/heure actuelle
     *
     * @param amountPaid Montant payé
     */
    public void markAsPaid (BigDecimal amountPaid) {
        this.paidAmount = this.paidAmount.add(amountPaid);
        this.paidAt = LocalDateTime.now();
    }

    public UUID getInstallmentPlanId () {
        return installmentPlanId;
    }

    public void setInstallmentPlanId (UUID installmentPlanId) {
        this.installmentPlanId = installmentPlanId;
    }

    public Integer getInstallmentNumber () {
        return installmentNumber;
    }

    public void setInstallmentNumber (Integer installmentNumber) {
        this.installmentNumber = installmentNumber;
    }

    public LocalDate getDueDate () {
        return dueDate;
    }

    public void setDueDate (LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public @NotNull(message = "Amount is required") BigDecimal getAmount () {
        return amount;
    }

    public void setAmount (@NotNull(message = "Amount is required") BigDecimal amount) {
        this.amount = amount;
    }

    public @NotNull(message = "Paid amount is required") BigDecimal getPaidAmount () {
        return paidAmount;
    }

    public void setPaidAmount (@NotNull(message = "Paid amount is required") BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public @NotNull(message = "Penalty amount is required") BigDecimal getPenaltyAmount () {
        return penaltyAmount;
    }

    public void setPenaltyAmount (@NotNull(message = "Penalty amount is required") BigDecimal penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public LocalDateTime getPaidAt () {
        return paidAt;
    }

    public void setPaidAt (LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public InstallmentPlanModel getInstallmentPlan () {
        return installmentPlan;
    }

    public void setInstallmentPlan (InstallmentPlanModel installmentPlan) {
        this.installmentPlan = installmentPlan;
    }

    public List<PaymentTransactionModel> getPaymentTransactions () {
        return paymentTransactions;
    }

    public void setPaymentTransactions (List<PaymentTransactionModel> paymentTransactions) {
        this.paymentTransactions = paymentTransactions;
    }
}
