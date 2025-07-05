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
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Représente un panier d'achat contenant les articles sélectionnés par un utilisateur
 */
@Entity
@Table(name = "carts", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CartModel extends BaseModel {

    /**
     * Identifiant de l'utilisateur propriétaire du panier Clé étrangère vers UserModel
     */
    @Column(name = "user_id")
    private UUID userId;

    /**
     * Statut courant du panier (ex: "ACTIVE", "ABANDONED", "CONVERTED_TO_ORDER")
     * Doit correspondre à un code existant dans le système
     */
    @Size(max = 20, message = "Le code statut ne doit pas dépasser 20 caractères")
    @Column(name = "status_code", length = 20)
    private String statusCode;

    /**
     * Montant total du panier (somme des articles)
     * Obligatoire - Doit être positif ou zéro
     * Valeur par défaut: 0.00
     */
    @NotNull(message = "Le montant total est requis")
    @DecimalMin(value = "0.00", message = "Le montant total ne peut être négatif")
    @Digits(integer = 10, fraction = 2, message = "Format de montant invalide (10 chiffres avant, 2 après la virgule)")
    @Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    /**
     * Date d'expiration du panier (si applicable)
     * Après cette date, le panier est considéré comme abandonné
     */
    @Future(message = "La date d'expiration doit être dans le futur")
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    /**
     * Référence vers l'utilisateur propriétaire du panier Relation Many-to-One vers UserModel Ne fait pas partie de la
     * sérialisation JSON
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnore
    private UserModel user;

    /**
     * Liste des articles contenus dans ce panier
     * Relation One-to-Many vers CartItemModel
     * Ne fait pas partie de la sérialisation JSON
     * Cascade: les opérations sur le panier affectent ses articles
     */
    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<CartItemModel> items = new ArrayList<>();


    /**
     * Vérifie si le panier est expiré
     * @return true si la date d'expiration est passée, false sinon
     */
    public boolean isExpired () {
        return expiresAt != null && expiresAt.isBefore(LocalDateTime.now());
    }

    /**
     * Calcule le nombre total d'articles dans le panier
     *
     * @return La somme des quantités de tous les articles
     */
    public int getTotalItemsCount () {
        return items.stream()
                .mapToInt(CartItemModel::getQuantity)
                .sum();
    }

    public UUID getUserId () {
        return userId;
    }

    public void setUserId (UUID userId) {
        this.userId = userId;
    }

    public String getStatusCode () {
        return statusCode;
    }

    public void setStatusCode (String statusCode) {
        this.statusCode = statusCode;
    }

    public @NotNull(message = "Total amount is required") BigDecimal getTotalAmount () {
        return totalAmount;
    }

    public void setTotalAmount (@NotNull(message = "Total amount is required") BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getExpiresAt () {
        return expiresAt;
    }

    public void setExpiresAt (LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public UserModel getUser () {
        return user;
    }

    public void setUser (UserModel user) {
        this.user = user;
    }

    public List<CartItemModel> getItems () {
        return items;
    }

    public void setItems (List<CartItemModel> items) {
        this.items = items;
    }
}