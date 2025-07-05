package sn.kredika_app.domain.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import sn.kredika_app.domain.dto.response.GatewayResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Représente une transaction de paiement dans le système. Enregistre tous les détails d'un paiement effectué par un
 * client.
 */
@Entity
@Table(name = "payment_transactions", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PaymentTransactionModel extends BaseModel {

    /**
     * Numéro unique de transaction généré par le système Format: "TRX-YYYYMMDD-XXXXX"
     */
    @Pattern(regexp = "^TRX-\\d{8}-\\d{5}$", message = "Le format du numéro de transaction est invalide")
    @Column(name = "transaction_number", unique = true, length = 20)
    private String transactionNumber;

    /**
     * Identifiant de l'utilisateur effectuant le paiement Clé étrangère vers UserModel
     */
    @Column(name = "user_id")
    private UUID userId;

    /**
     * Identifiant du plan de paiement échelonné associé (si applicable)
     * Clé étrangère vers InstallmentPlanModel
     */
    @Column(name = "installment_plan_id")
    private UUID installmentPlanId;

    /**
     * Identifiant de l'échéance de paiement associée (si applicable)
     * Clé étrangère vers PaymentScheduleModel
     */
    @Column(name = "payment_schedule_id")
    private UUID paymentScheduleId;

    /**
     * Montant de la transaction
     * Doit être strictement positif
     */
    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant doit être positif")
    @Digits(integer = 10, fraction = 2, message = "Format de montant invalide (10 chiffres avant, 2 après la virgule)")
    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    /**
     * Méthode de paiement utilisée (CARTE_BANCAIRE, MOBILE_MONEY, VIREMENT, etc.)
     */
    @Size(max = 30, message = "Le code de méthode de paiement ne peut excéder 30 caractères")
    @Column(name = "payment_method_code", length = 30)
    private String paymentMethodCode;

    /**
     * Identifiant de transaction du système externe (banque, mobile money, etc.)
     */
    @Size(max = 100, message = "L'identifiant externe ne peut excéder 100 caractères")
    @Column(name = "external_transaction_id", length = 100)
    private String externalTransactionId;

    /**
     * Réponse brute de la passerelle de paiement au format JSON
     * Contient les détails techniques de la transaction
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "gateway_response", columnDefinition = "jsonb")
    private GatewayResponseDto gatewayResponse;

    /**
     * Statut courant de la transaction (PENDING, SUCCESS, FAILED, REFUNDED)
     */
    @Size(max = 20, message = "Le code statut ne peut excéder 20 caractères")
    @Column(name = "status_code", length = 20)
    private String statusCode = "PENDING";

    /**
     * Date et heure de traitement effectif du paiement
     */
    @PastOrPresent(message = "La date de traitement doit être dans le passé ou présent")
    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    /**
     * Raison de l'échec si la transaction a échoué
     */
    @Size(max = 500, message = "La raison d'échec ne peut excéder 500 caractères")
    @Column(name = "failure_reason", columnDefinition = "TEXT")
    private String failureReason;

    /**
     * Date et heure de remboursement si applicable
     */
    @PastOrPresent(message = "La date de remboursement doit être dans le passé ou présent")
    @Column(name = "refunded_at")
    private LocalDateTime refundedAt;

    /**
     * Montant remboursé si applicable
     * Doit être positif ou nul et inférieur au montant initial
     */
    @DecimalMin(value = "0.00", message = "Le montant remboursé ne peut être négatif")
    @Digits(integer = 10, fraction = 2, message = "Format de montant invalide (10 chiffres avant, 2 après la virgule)")
    @Column(name = "refund_amount", precision = 10, scale = 2)
    private BigDecimal refundAmount = BigDecimal.ZERO;

    /**
     * Référence vers l'utilisateur effectuant le paiement
     * Relation Many-to-One vers UserModel
     * Ne fait pas partie de la sérialisation JSON
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnore
    private UserModel user;

    /**
     * Référence vers le plan de paiement échelonné associé
     * Relation Many-to-One vers InstallmentPlanModel
     * Ne fait pas partie de la sérialisation JSON
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "installment_plan_id", insertable = false, updatable = false)
    @JsonIgnore
    private InstallmentPlanModel installmentPlan;

    /**
     * Référence vers l'échéance de paiement associée
     * Relation Many-to-One vers PaymentScheduleModel
     * Ne fait pas partie de la sérialisation JSON
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_schedule_id", insertable = false, updatable = false)
    @JsonIgnore
    private PaymentScheduleModel paymentSchedule;

    /**
     * Marque la transaction comme réussie
     *
     * @param externalId Identifiant externe de la transaction
     * @param response   Réponse de la passerelle de paiement
     */
    public void markAsSuccessful (String externalId, GatewayResponseDto response) {
        this.statusCode = "SUCCESS";
        this.externalTransactionId = externalId;
        this.gatewayResponse = response;
        this.processedAt = LocalDateTime.now();
        this.failureReason = null;
    }

    /**
     * Marque la transaction comme échouée
     *
     * @param reason Raison de l'échec
     */
    public void markAsFailed (String reason) {
        this.statusCode = "FAILED";
        this.processedAt = LocalDateTime.now();
        this.failureReason = reason;
    }

    /**
     * Effectue un remboursement partiel ou total
     *
     * @param refundAmount Montant à rembourser
     */
    public void processRefund (BigDecimal refundAmount) {
        if (refundAmount.compareTo(BigDecimal.ZERO) <= 0 ||
                refundAmount.compareTo(this.amount) > 0) {
            throw new IllegalArgumentException("Montant de remboursement invalide");
        }

        this.refundAmount = refundAmount;
        this.refundedAt = LocalDateTime.now();
        this.statusCode = "REFUNDED";
    }

    /**
     * Vérifie si la transaction a été remboursée
     *
     * @return true si refundAmount > 0, false sinon
     */
    public boolean isRefunded () {
        return refundAmount.compareTo(BigDecimal.ZERO) > 0;
    }

    public String getTransactionNumber () {
        return transactionNumber;
    }

    public void setTransactionNumber (String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public UUID getUserId () {
        return userId;
    }

    public void setUserId (UUID userId) {
        this.userId = userId;
    }

    public UUID getInstallmentPlanId () {
        return installmentPlanId;
    }

    public void setInstallmentPlanId (UUID installmentPlanId) {
        this.installmentPlanId = installmentPlanId;
    }

    public UUID getPaymentScheduleId () {
        return paymentScheduleId;
    }

    public void setPaymentScheduleId (UUID paymentScheduleId) {
        this.paymentScheduleId = paymentScheduleId;
    }

    public @NotNull(message = "Amount is required") BigDecimal getAmount () {
        return amount;
    }

    public void setAmount (@NotNull(message = "Amount is required") BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethodCode () {
        return paymentMethodCode;
    }

    public void setPaymentMethodCode (String paymentMethodCode) {
        this.paymentMethodCode = paymentMethodCode;
    }

    public String getExternalTransactionId () {
        return externalTransactionId;
    }

    public void setExternalTransactionId (String externalTransactionId) {
        this.externalTransactionId = externalTransactionId;
    }

    public GatewayResponseDto getGatewayResponse () {
        return gatewayResponse;
    }

    public void setGatewayResponse (GatewayResponseDto gatewayResponse) {
        this.gatewayResponse = gatewayResponse;
    }

    public String getStatusCode () {
        return statusCode;
    }

    public void setStatusCode (String statusCode) {
        this.statusCode = statusCode;
    }

    public LocalDateTime getProcessedAt () {
        return processedAt;
    }

    public void setProcessedAt (LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public String getFailureReason () {
        return failureReason;
    }

    public void setFailureReason (String failureReason) {
        this.failureReason = failureReason;
    }

    public LocalDateTime getRefundedAt () {
        return refundedAt;
    }

    public void setRefundedAt (LocalDateTime refundedAt) {
        this.refundedAt = refundedAt;
    }

    public BigDecimal getRefundAmount () {
        return refundAmount;
    }

    public void setRefundAmount (BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public UserModel getUser () {
        return user;
    }

    public void setUser (UserModel user) {
        this.user = user;
    }

    public InstallmentPlanModel getInstallmentPlan () {
        return installmentPlan;
    }

    public void setInstallmentPlan (InstallmentPlanModel installmentPlan) {
        this.installmentPlan = installmentPlan;
    }

    public PaymentScheduleModel getPaymentSchedule () {
        return paymentSchedule;
    }

    public void setPaymentSchedule (PaymentScheduleModel paymentSchedule) {
        this.paymentSchedule = paymentSchedule;
    }
}