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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderModel extends BaseModel {

    @NotBlank(message = "Order number is required")
    @Column(name = "order_number", nullable = false)
    private String orderNumber;

    @NotNull(message = "User ID is required")
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "delivery_address_id")
    private UUID deliveryAddressId;

    @NotNull(message = "Total amount is required")
    @Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "status_code")
    private String statusCode;

    @Column(name = "payment_status_code")
    private String paymentStatusCode;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnore
    private UserModel user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_address_id", insertable = false, updatable = false)
    @JsonIgnore
    private UserAddressModel deliveryAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<OrderItemModel> items = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<InstallmentPlanModel> installmentPlans = new ArrayList<>();

    public @NotBlank(message = "Order number is required") String getOrderNumber () {
        return orderNumber;
    }

    public void setOrderNumber (@NotBlank(message = "Order number is required") String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public @NotNull(message = "User ID is required") UUID getUserId () {
        return userId;
    }

    public void setUserId (@NotNull(message = "User ID is required") UUID userId) {
        this.userId = userId;
    }

    public UUID getDeliveryAddressId () {
        return deliveryAddressId;
    }

    public void setDeliveryAddressId (UUID deliveryAddressId) {
        this.deliveryAddressId = deliveryAddressId;
    }

    public @NotNull(message = "Total amount is required") BigDecimal getTotalAmount () {
        return totalAmount;
    }

    public void setTotalAmount (@NotNull(message = "Total amount is required") BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatusCode () {
        return statusCode;
    }

    public void setStatusCode (String statusCode) {
        this.statusCode = statusCode;
    }

    public String getPaymentStatusCode () {
        return paymentStatusCode;
    }

    public void setPaymentStatusCode (String paymentStatusCode) {
        this.paymentStatusCode = paymentStatusCode;
    }

    public String getNotes () {
        return notes;
    }

    public void setNotes (String notes) {
        this.notes = notes;
    }

    public LocalDateTime getDeliveryDate () {
        return deliveryDate;
    }

    public void setDeliveryDate (LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
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

    public UserAddressModel getDeliveryAddress () {
        return deliveryAddress;
    }

    public void setDeliveryAddress (UserAddressModel deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public List<OrderItemModel> getItems () {
        return items;
    }

    public void setItems (List<OrderItemModel> items) {
        this.items = items;
    }

    public List<InstallmentPlanModel> getInstallmentPlans () {
        return installmentPlans;
    }

    public void setInstallmentPlans (List<InstallmentPlanModel> installmentPlans) {
        this.installmentPlans = installmentPlans;
    }
}