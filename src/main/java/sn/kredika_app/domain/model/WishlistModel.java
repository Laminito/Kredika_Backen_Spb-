package sn.kredika_app.domain.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Modèle représentant un produit dans la liste de souhaits d'un utilisateur. Permet à un utilisateur d'enregistrer des
 * produits pour un achat ultérieur.
 */
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

    /**
     * Identifiant de l'utilisateur propriétaire de la liste de souhaits. Ce champ est obligatoire.
     */
    @NotNull(message = "L'utilisateur est requis")
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    /**
     * Identifiant du produit ajouté à la liste de souhaits. Ce champ est obligatoire.
     */
    @NotNull(message = "Le produit est requis")
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    /**
     * Notes personnelles de l'utilisateur sur ce produit. Limitée à 500 caractères maximum.
     */
    @Size(max = 500, message = "Les notes ne peuvent excéder 500 caractères")
    @Column(name = "notes", length = 500)
    private String notes;

    /**
     * Priorité du produit dans la liste (1-5).
     * 1 = priorité la plus basse, 5 = priorité la plus haute.
     * Par défaut à 1.
     */
    @Min(value = 1, message = "La priorité minimale est 1")
    @Max(value = 5, message = "La priorité maximale est 5")
    @Column(name = "priority")
    private Integer priority = 1;

    /**
     * Utilisateur associé à cette entrée de liste de souhaits. Relation ManyToOne chargée en mode LAZY.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnore
    private UserModel user;

    /**
     * Produit associé à cette entrée de liste de souhaits.
     * Relation ManyToOne chargée en mode LAZY.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    @JsonIgnore
    private ProductModel product;

    /**
     * Augmente la priorité du produit dans la liste.
     * Ne dépasse pas la priorité maximale (5).
     */
    public void increasePriority () {
        if (priority < 5) {
            priority++;
        }
    }

    /**
     * Diminue la priorité du produit dans la liste. Ne descend pas en dessous de la priorité minimale (1).
     */
    public void decreasePriority () {
        if (priority > 1) {
            priority--;
        }
    }

    /**
     * Vérifie si le produit est en haute priorité (4 ou 5).
     *
     * @return true si haute priorité, false sinon
     */
    public boolean isHighPriority () {
        return priority >= 4;
    }

    /**
     * Vérifie si des notes sont présentes pour ce produit.
     *
     * @return true si des notes existent, false sinon
     */
    public boolean hasNotes () {
        return notes != null && !notes.trim().isEmpty();
    }

    /**
     * Formatte les informations de base de l'entrée de liste de souhaits.
     *
     * @return une représentation textuelle de l'entrée
     */
    public String getDisplayInfo () {
        return String.format(
                "Produit: %s | Priorité: %d/5 | Notes: %s",
                product != null ? product.getName() : productId,
                priority,
                hasNotes() ? notes : "Aucune note"
        );
    }

    /**
     * Vérifie si cette entrée concerne un produit donné.
     *
     * @param productId l'identifiant du produit à vérifier
     * @return true si c'est le même produit, false sinon
     */
    public boolean isForProduct (UUID productId) {
        return this.productId.equals(productId);
    }

    /**
     * Vérifie si cette entrée appartient à un utilisateur donné.
     *
     * @param userId l'identifiant de l'utilisateur à vérifier
     * @return true si c'est le même utilisateur, false sinon
     */
    public boolean isOwnedBy (UUID userId) {
        return this.userId.equals(userId);
    }

    /**
     * Met à jour les notes de l'entrée.
     *
     * @param newNotes les nouvelles notes à enregistrer
     */
    public void updateNotes (String newNotes) {
        this.notes = (newNotes != null && !newNotes.trim().isEmpty()) ? newNotes.trim() : null;
    }

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