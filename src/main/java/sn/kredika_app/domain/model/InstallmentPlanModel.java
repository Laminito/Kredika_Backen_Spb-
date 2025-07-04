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
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "installment_plans", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InstallmentPlanModel extends BaseModel {

    @Column(name = "plan_number")
    private String planNumber;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "product_id")
    private UUID productId;

    @NotNull(message = "Principal amount is required")
    @Column(name = "principal_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal principalAmount;

    @NotNull(message = "Commission rate is required")
    @Column(name = "commission_rate", precision = 5, scale = 4, nullable = false)
    private BigDecimal commissionRate;

    @NotNull(message = "Commission amount is required")
    @Column(name = "commission_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal commissionAmount;

    @NotNull(message = "Total amount is required")
    @Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @NotNull(message = "Installment amount is required")
    @Column(name = "installment_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal installmentAmount;

    @Column(name = "duration_months")
    private Integer durationMonths;

    @Column(name = "frequency_code")
    private String frequencyCode;

    @Column(name = "total_installments")
    private Integer totalInstallments;

    @Column(name = "paid_installments")
    private Integer paidInstallments;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "status_code")
    private String statusCode;

    @Column(name = "late_penalty", precision = 10, scale = 2)
    private BigDecimal latePenalty = BigDecimal.ZERO;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    // Relations
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

    @OneToMany(mappedBy = "installmentPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PaymentScheduleModel> paymentSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "installmentPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PaymentTransactionModel> paymentTransactions = new ArrayList<>();

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