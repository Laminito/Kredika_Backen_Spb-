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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "payment_schedules", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PaymentScheduleModel extends BaseModel {

    @Column(name = "installment_plan_id")
    private UUID installmentPlanId;

    @Column(name = "installment_number")
    private Integer installmentNumber;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @NotNull(message = "Amount is required")
    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull(message = "Paid amount is required")
    @Column(name = "paid_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @NotNull(message = "Penalty amount is required")
    @Column(name = "penalty_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal penaltyAmount = BigDecimal.ZERO;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "installment_plan_id", insertable = false, updatable = false)
    @JsonIgnore
    private InstallmentPlanModel installmentPlan;

    @OneToMany(mappedBy = "paymentSchedule", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PaymentTransactionModel> paymentTransactions = new ArrayList<>();

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
