package sn.kredika_app.domain.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "cart_items", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CartItemModel extends BaseModel {

    @NotNull(message = "Cart ID is required")
    @Column(name = "cart_id", nullable = false)
    private UUID cartId;

    @NotNull(message = "Product ID is required")
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "payment_method_code")
    private String paymentMethodCode;

    @Column(name = "credit_duration")
    private Integer creditDuration;

    @Column(name = "credit_frequency_code")
    private String creditFrequencyCode;

    @NotNull(message = "Unit price is required")
    @Column(name = "unit_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "commission_rate", precision = 5, scale = 4)
    private BigDecimal commissionRate;

    @NotNull(message = "Total amount is required")
    @Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "installment_amount", precision = 10, scale = 2)
    private BigDecimal installmentAmount;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", insertable = false, updatable = false)
    @JsonIgnore
    private CartModel cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    @JsonIgnore
    private ProductModel product;

    public @NotNull(message = "Cart ID is required") UUID getCartId () {
        return cartId;
    }

    public void setCartId (@NotNull(message = "Cart ID is required") UUID cartId) {
        this.cartId = cartId;
    }

    public @NotNull(message = "Product ID is required") UUID getProductId () {
        return productId;
    }

    public void setProductId (@NotNull(message = "Product ID is required") UUID productId) {
        this.productId = productId;
    }

    public Integer getQuantity () {
        return quantity;
    }

    public void setQuantity (Integer quantity) {
        this.quantity = quantity;
    }

    public String getPaymentMethodCode () {
        return paymentMethodCode;
    }

    public void setPaymentMethodCode (String paymentMethodCode) {
        this.paymentMethodCode = paymentMethodCode;
    }

    public Integer getCreditDuration () {
        return creditDuration;
    }

    public void setCreditDuration (Integer creditDuration) {
        this.creditDuration = creditDuration;
    }

    public String getCreditFrequencyCode () {
        return creditFrequencyCode;
    }

    public void setCreditFrequencyCode (String creditFrequencyCode) {
        this.creditFrequencyCode = creditFrequencyCode;
    }

    public @NotNull(message = "Unit price is required") BigDecimal getUnitPrice () {
        return unitPrice;
    }

    public void setUnitPrice (@NotNull(message = "Unit price is required") BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getCommissionRate () {
        return commissionRate;
    }

    public void setCommissionRate (BigDecimal commissionRate) {
        this.commissionRate = commissionRate;
    }

    public @NotNull(message = "Total amount is required") BigDecimal getTotalAmount () {
        return totalAmount;
    }

    public void setTotalAmount (@NotNull(message = "Total amount is required") BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getInstallmentAmount () {
        return installmentAmount;
    }

    public void setInstallmentAmount (BigDecimal installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public CartModel getCart () {
        return cart;
    }

    public void setCart (CartModel cart) {
        this.cart = cart;
    }

    public ProductModel getProduct () {
        return product;
    }

    public void setProduct (ProductModel product) {
        this.product = product;
    }
}
