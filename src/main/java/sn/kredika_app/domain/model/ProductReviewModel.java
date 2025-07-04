package sn.kredika_app.domain.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "product_reviews", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductReviewModel extends BaseModel {

    @NotNull(message = "Le produit est requis")
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @NotNull(message = "L'utilisateur est requis")
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "order_id")
    private UUID orderId;

    @NotNull(message = "La note est requise")
    @Min(value = 1, message = "La note doit être entre 1 et 5")
    @Max(value = 5, message = "La note doit être entre 1 et 5")
    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Size(max = 100, message = "Le titre ne peut excéder 100 caractères")
    @Column(name = "title")
    private String title;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "is_verified_purchase")
    private Boolean isVerifiedPurchase = false;

    @Column(name = "is_approved")
    private Boolean isApproved = false;

    @Column(name = "helpful_count")
    private Integer helpfulCount = 0;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    @JsonIgnore
    private ProductModel product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnore
    private UserModel user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    @JsonIgnore
    private OrderModel order;

    public @NotNull(message = "Le produit est requis") UUID getProductId () {
        return productId;
    }

    public void setProductId (@NotNull(message = "Le produit est requis") UUID productId) {
        this.productId = productId;
    }

    public @NotNull(message = "L'utilisateur est requis") UUID getUserId () {
        return userId;
    }

    public void setUserId (@NotNull(message = "L'utilisateur est requis") UUID userId) {
        this.userId = userId;
    }

    public UUID getOrderId () {
        return orderId;
    }

    public void setOrderId (UUID orderId) {
        this.orderId = orderId;
    }

    public @NotNull(message = "La note est requise") @Min(value = 1, message = "La note doit être entre 1 et 5") @Max(value = 5, message = "La note doit être entre 1 et 5") Integer getRating () {
        return rating;
    }

    public void setRating (@NotNull(message = "La note est requise") @Min(value = 1, message = "La note doit être entre 1 et 5") @Max(value = 5, message = "La note doit être entre 1 et 5") Integer rating) {
        this.rating = rating;
    }

    public @Size(max = 100, message = "Le titre ne peut excéder 100 caractères") String getTitle () {
        return title;
    }

    public void setTitle (@Size(max = 100, message = "Le titre ne peut excéder 100 caractères") String title) {
        this.title = title;
    }

    public String getComment () {
        return comment;
    }

    public void setComment (String comment) {
        this.comment = comment;
    }

    public Boolean getVerifiedPurchase () {
        return isVerifiedPurchase;
    }

    public void setVerifiedPurchase (Boolean verifiedPurchase) {
        isVerifiedPurchase = verifiedPurchase;
    }

    public Boolean getApproved () {
        return isApproved;
    }

    public void setApproved (Boolean approved) {
        isApproved = approved;
    }

    public Integer getHelpfulCount () {
        return helpfulCount;
    }

    public void setHelpfulCount (Integer helpfulCount) {
        this.helpfulCount = helpfulCount;
    }

    public ProductModel getProduct () {
        return product;
    }

    public void setProduct (ProductModel product) {
        this.product = product;
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
}
