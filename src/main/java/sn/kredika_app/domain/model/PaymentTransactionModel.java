package sn.kredika_app.domain.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment_transactions", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PaymentTransactionModel extends BaseModel {

    @Column(name = "transaction_number")
    private String transactionNumber;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "installment_plan_id")
    private UUID installmentPlanId;

    @Column(name = "payment_schedule_id")
    private UUID paymentScheduleId;

    @NotNull(message = "Amount is required")
    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "payment_method_code")
    private String paymentMethodCode;

    @Column(name = "external_transaction_id")
    private String externalTransactionId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "gateway_response", columnDefinition = "jsonb")
    private Object gatewayResponse;

    @Column(name = "status_code")
    private String statusCode;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "failure_reason", columnDefinition = "TEXT")
    private String failureReason;

    @Column(name = "refunded_at")
    private LocalDateTime refundedAt;

    @Column(name = "refund_amount", precision = 10, scale = 2)
    private BigDecimal refundAmount = BigDecimal.ZERO;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnore
    private UserModel user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "installment_plan_id", insertable = false, updatable = false)
    @JsonIgnore
    private InstallmentPlanModel installmentPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_schedule_id", insertable = false, updatable = false)
    @JsonIgnore
    private PaymentScheduleModel paymentSchedule;

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

    public Object getGatewayResponse () {
        return gatewayResponse;
    }

    public void setGatewayResponse (Object gatewayResponse) {
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