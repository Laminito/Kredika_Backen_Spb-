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
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_items", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderItemModel extends BaseModel {

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "quantity")
    private Integer quantity;

    @NotNull(message = "Unit price is required")
    @Column(name = "unit_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @NotNull(message = "Total price is required")
    @Column(name = "total_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "payment_method_code")
    private String paymentMethodCode;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    @JsonIgnore
    private OrderModel order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    @JsonIgnore
    private ProductModel product;


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

    public Integer getQuantity () {
        return quantity;
    }

    public void setQuantity (Integer quantity) {
        this.quantity = quantity;
    }

    public @NotNull(message = "Unit price is required") BigDecimal getUnitPrice () {
        return unitPrice;
    }

    public void setUnitPrice (@NotNull(message = "Unit price is required") BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public @NotNull(message = "Total price is required") BigDecimal getTotalPrice () {
        return totalPrice;
    }

    public void setTotalPrice (@NotNull(message = "Total price is required") BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentMethodCode () {
        return paymentMethodCode;
    }

    public void setPaymentMethodCode (String paymentMethodCode) {
        this.paymentMethodCode = paymentMethodCode;
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
}