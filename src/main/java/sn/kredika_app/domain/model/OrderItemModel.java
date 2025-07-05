package sn.kredika_app.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Représente un article spécifique dans une commande client. Contient les détails de prix, quantité et produit pour
 * chaque ligne de commande.
 */
@Entity
@Table(name = "order_items", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderItemModel extends BaseModel {

    /**
     * Identifiant de la commande parente Clé étrangère vers OrderModel
     */
    @NotNull(message = "L'identifiant de commande est obligatoire")
    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    /**
     * Identifiant du produit commandé
     * Clé étrangère vers ProductModel
     */
    @NotNull(message = "L'identifiant du produit est obligatoire")
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    /**
     * Quantité commandée du produit
     * Doit être au minimum 1
     */
    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 1, message = "La quantité minimale est 1")
    @Max(value = 999, message = "La quantité maximale est 999")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /**
     * Prix unitaire du produit au moment de la commande
     * Doit être strictement positif
     */
    @NotNull(message = "Le prix unitaire est obligatoire")
    @DecimalMin(value = "0.01", message = "Le prix unitaire doit être positif")
    @Digits(integer = 10, fraction = 2, message = "Format de prix invalide (10 chiffres avant, 2 après la virgule)")
    @Column(name = "unit_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    /**
     * Prix total pour cette ligne (quantité × prix unitaire)
     * Doit être cohérent avec le calcul quantité × prix unitaire
     */
    @NotNull(message = "Le prix total est obligatoire")
    @DecimalMin(value = "0.01", message = "Le prix total doit être positif")
    @Digits(integer = 10, fraction = 2, message = "Format de prix invalide (10 chiffres avant, 2 après la virgule)")
    @Column(name = "total_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    /**
     * Méthode de paiement spécifique pour cet article (si différente de la commande)
     * Ex: "CASH", "CREDIT", "MOBILE_MONEY"
     */
    @Size(max = 20, message = "Le code de méthode de paiement ne peut excéder 20 caractères")
    @Column(name = "payment_method_code", length = 20)
    private String paymentMethodCode;


    /**
     * Référence vers la commande parente Relation Many-to-One vers OrderModel Ne fait pas partie de la sérialisation
     * JSON
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    @JsonIgnore
    private OrderModel order;

    /**
     * Référence vers le produit commandé
     * Relation Many-to-One vers ProductModel
     * Ne fait pas partie de la sérialisation JSON
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    @JsonIgnore
    private ProductModel product;

    /**
     * Calcule automatiquement le prix total avant persistance
     */
    @PreUpdate
    private void calculateTotalPrice () {
        if (quantity != null && unitPrice != null) {
            this.totalPrice = unitPrice.multiply(new BigDecimal(quantity));
        }
    }

    /**
     * Vérifie si l'article est payé par crédit
     *
     * @return true si paymentMethodCode est "CREDIT", false sinon
     */
    public boolean isCreditPayment () {
        return "CREDIT".equals(paymentMethodCode);
    }

    /**
     * Vérifie la cohérence des prix (unitaire vs total)
     *
     * @return true si totalPrice = unitPrice * quantity, false sinon
     */
    public boolean isPriceConsistent () {
        if (unitPrice == null || quantity == null || totalPrice == null) {
            return false;
        }
        BigDecimal expectedTotal = unitPrice.multiply(new BigDecimal(quantity));
        return totalPrice.compareTo(expectedTotal) == 0;
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