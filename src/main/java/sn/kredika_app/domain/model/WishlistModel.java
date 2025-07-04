package sn.kredika_app.domain.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(
        name = "wishlists", schema = "kredika_app",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_wishlist_user_product",
                        columnNames = {"user_id", "product_id"}
                )
        }
)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WishlistModel extends BaseModel {

    @NotNull(message = "L'utilisateur est requis")
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @NotNull(message = "Le produit est requis")
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "notes")
    private String notes;

    @Column(name = "priority")
    private Integer priority = 1;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnore
    private UserModel user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    @JsonIgnore
    private ProductModel product;

    public @NotNull(message = "L'utilisateur est requis") UUID getUserId () {
        return userId;
    }

    public void setUserId (@NotNull(message = "L'utilisateur est requis") UUID userId) {
        this.userId = userId;
    }

    public @NotNull(message = "Le produit est requis") UUID getProductId () {
        return productId;
    }

    public void setProductId (@NotNull(message = "Le produit est requis") UUID productId) {
        this.productId = productId;
    }

    public String getNotes () {
        return notes;
    }

    public void setNotes (String notes) {
        this.notes = notes;
    }

    public Integer getPriority () {
        return priority;
    }

    public void setPriority (Integer priority) {
        this.priority = priority;
    }

    public UserModel getUser () {
        return user;
    }

    public void setUser (UserModel user) {
        this.user = user;
    }

    public ProductModel getProduct () {
        return product;
    }

    public void setProduct (ProductModel product) {
        this.product = product;
    }
}