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

/**
 * Modèle représentant un avis sur un produit.
 */
@Entity
@Table(name = "product_reviews", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductReviewModel extends BaseModel {

    /**
     * Identifiant du produit concerné par l'avis. Ce champ est obligatoire.
     */
    @NotNull(message = "Le produit est requis")
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    /**
     * Identifiant de l'utilisateur qui a posté l'avis. Ce champ est obligatoire.
     */
    @NotNull(message = "L'utilisateur est requis")
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    /**
     * Identifiant de la commande associée à l'avis (optionnel). Peut être utilisé pour vérifier si l'utilisateur a
     * effectivement acheté le produit.
     */
    @Column(name = "order_id")
    private UUID orderId;

    /**
     * Note attribuée au produit (entre 1 et 5). Ce champ est obligatoire.
     */
    @NotNull(message = "La note est requise")
    @Min(value = 1, message = "La note doit être entre 1 et 5")
    @Max(value = 5, message = "La note doit être entre 1 et 5")
    @Column(name = "rating", nullable = false)
    private Integer rating;

    /**
     * Titre de l'avis. Limitée à 100 caractères maximum.
     */
    @Size(max = 100, message = "Le titre ne peut excéder 100 caractères")
    @Column(name = "title")
    private String title;

    /**
     * Commentaire détaillé sur le produit. Stocké sous forme de texte dans la base de données.
     */
    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    /**
     * Indique si l'avis provient d'un achat vérifié. Par défaut à false.
     */
    @Column(name = "is_verified_purchase")
    private Boolean isVerifiedPurchase = false;

    /**
     * Indique si l'avis a été approuvé par un modérateur. Par défaut à false.
     */
    @Column(name = "is_approved")
    private Boolean isApproved = false;

    /**
     * Nombre d'utilisateurs ayant trouvé cet avis utile. Par défaut à 0.
     */
    @Column(name = "helpful_count")
    private Integer helpfulCount = 0;

    /**
     * Relation ManyToOne avec le produit concerné. Chargée en mode LAZY pour optimiser les performances.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    @JsonIgnore
    private ProductModel product;

    /**
     * Relation ManyToOne avec l'utilisateur ayant posté l'avis. Chargée en mode LAZY pour optimiser les performances.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnore
    private UserModel user;

    /**
     * Relation ManyToOne avec la commande associée. Chargée en mode LAZY pour optimiser les performances.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    @JsonIgnore
    private OrderModel order;

    // Méthodes utilitaires

    /**
     * Marque cet avis comme achat vérifié.
     */
    public void markAsVerifiedPurchase () {
        this.isVerifiedPurchase = true;
    }

    /**
     * Approuve cet avis.
     */
    public void approve () {
        this.isApproved = true;
    }

    /**
     * Incrémente le compteur d'avis utiles.
     *
     * @return le nouveau nombre de votes "utile"
     */
    public int incrementHelpfulCount () {
        this.helpfulCount = (this.helpfulCount == null) ? 1 : this.helpfulCount + 1;
        return this.helpfulCount;
    }

    /**
     * Vérifie si l'avis est positif (note de 4 ou 5).
     *
     * @return true si l'avis est positif, false sinon
     */
    public boolean isPositiveReview () {
        return this.rating != null && this.rating >= 4;
    }

    /**
     * Vérifie si l'avis est négatif (note de 1 ou 2).
     *
     * @return true si l'avis est négatif, false sinon
     */
    public boolean isNegativeReview () {
        return this.rating != null && this.rating <= 2;
    }

    /**
     * Vérifie si l'avis est neutre (note de 3).
     *
     * @return true si l'avis est neutre, false sinon
     */
    public boolean isNeutralReview () {
        return this.rating != null && this.rating == 3;
    }

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
