package sn.kredika_app.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "credit_profiles", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreditProfileModel extends BaseModel {

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "credit_score")
    private Integer creditScore;

    @NotNull(message = "Credit limit is required")
    @Column(name = "credit_limit", precision = 10, scale = 2, nullable = false)
    private BigDecimal creditLimit;

    @NotNull(message = "Available credit is required")
    @Column(name = "available_credit", precision = 10, scale = 2, nullable = false)
    private BigDecimal availableCredit;

    @NotNull(message = "Total debt is required")
    @Column(name = "total_debt", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalDebt = BigDecimal.ZERO;

    @Column(name = "default_count")
    private Integer defaultCount;

    @Column(name = "last_credit_review")
    private LocalDate lastCreditReview;

    // Relations
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnore
    private UserModel user;

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