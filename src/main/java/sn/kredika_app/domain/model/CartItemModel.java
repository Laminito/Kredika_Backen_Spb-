package sn.kredika_app.domain.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;


/**
 * Représente un article dans le panier d'achat d'un utilisateur
 */
@Entity
@Table(name = "cart_items", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CartItemModel extends BaseModel {

    /**
     * ID du panier auquel cet article appartient Obligatoire - Clé étrangère vers CartModel
     */
    @NotNull(message = "L'ID du panier est requis")
    @Column(name = "cart_id", nullable = false)
    private UUID cartId;

    /**
     * ID du produit associé à cet article
     * Obligatoire - Clé étrangère vers ProductModel
     */
    @NotNull(message = "L'ID du produit est requis")
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    /**
     * Quantité du produit dans le panier
     */
    @Min(value = 1, message = "La quantité doit être au moins 1")
    @Column(name = "quantity")
    private Integer quantity ;

    /**
     * Méthode de paiement choisie pour cet article
     * Ex: "CASH", "CREDIT", "MOBILE_MONEY"
     */
    @Size(max = 20, message = "Le code de méthode de paiement ne doit pas dépasser 20 caractères")
    @Column(name = "payment_method_code", length = 20)
    private String paymentMethodCode;

    /**
     * Durée du crédit en mois (si paiement à crédit)
     * Doit être positif si spécifié
     */
    @Min(value = 1, message = "La durée du crédit doit être positive")
    @Column(name = "credit_duration")
    private Integer creditDuration;

    /**
     * Fréquence de paiement pour le crédit
     * Ex: "MONTHLY", "WEEKLY", "BIWEEKLY"
     */
    @Size(max = 20, message = "Le code de fréquence ne doit pas dépasser 20 caractères")
    @Column(name = "credit_frequency_code", length = 20)
    private String creditFrequencyCode;

    /**
     * Prix unitaire du produit au moment de l'ajout au panier
     * Obligatoire - Doit être positif
     */
    @NotNull(message = "Le prix unitaire est requis")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix unitaire doit être positif")
    @Digits(integer = 10, fraction = 2, message = "Format de prix invalide")
    @Column(name = "unit_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    /**
     * Taux de commission appliqué (entre 0 et 1)
     * Ex: 0.05 pour 5% de commission
     */
    @DecimalMin(value = "0.0", message = "Le taux de commission ne peut être négatif")
    @DecimalMax(value = "1.0", message = "Le taux de commission ne peut dépasser 100%")
    @Column(name = "commission_rate", precision = 5, scale = 4)
    private BigDecimal commissionRate;

    /**
     * Montant total pour cette ligne (quantité × prix unitaire)
     * Obligatoire - Doit être positif
     */
    @NotNull(message = "Le montant total est requis")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le montant total doit être positif")
    @Digits(integer = 10, fraction = 2, message = "Format de montant invalide")
    @Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    /**
     * Montant de chaque versement (si paiement à crédit)
     * Doit être positif si spécifié
     */
    @DecimalMin(value = "0.0", inclusive = false, message = "Le montant de l'échéance doit être positif")
    @Digits(integer = 10, fraction = 2, message = "Format de montant invalide")
    @Column(name = "installment_amount", precision = 10, scale = 2)
    private BigDecimal installmentAmount;

    /**
     * Référence vers le panier parent Relation Many-to-One vers CartModel
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", insertable = false, updatable = false)
    @JsonIgnore
    private CartModel cart;

    /**
     * Référence vers le produit associé
     * Relation Many-to-One vers ProductModel
     */
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
