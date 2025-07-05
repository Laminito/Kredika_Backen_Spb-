package sn.kredika_app.domain.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Représente une commande client dans le système e-commerce. Contient toutes les informations relatives à une
 * transaction commerciale.
 */
@Entity
@Table(name = "orders", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderModel extends BaseModel {

    /**
     * Numéro unique de commande (format: "CMD-YYYYMMDD-XXXXX") Généré automatiquement par le système
     */
    @NotBlank(message = "Le numéro de commande est obligatoire")
    @Pattern(regexp = "^CMD-\\d{8}-\\d{5}$", message = "Format de numéro de commande invalide")
    @Column(name = "order_number", nullable = false, unique = true, length = 20)
    private String orderNumber;

    /**
     * Identifiant de l'utilisateur ayant passé la commande
     * Clé étrangère vers UserModel
     */
    @NotNull(message = "L'identifiant utilisateur est obligatoire")
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    /**
     * Identifiant de l'adresse de livraison
     * Clé étrangère vers UserAddressModel
     */
    @Column(name = "delivery_address_id")
    private UUID deliveryAddressId;

    /**
     * Montant total de la commande (somme des articles + frais)
     * Doit être strictement positif
     */
    @NotNull(message = "Le montant total est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant total doit être positif")
    @Digits(integer = 10, fraction = 2, message = "Format de montant invalide (10 chiffres avant, 2 après la virgule)")
    @Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    /**
     * Statut courant de la commande (CREATED, PROCESSING, SHIPPED, DELIVERED, CANCELLED)
     * Doit correspondre à un code existant dans le système
     */
    @Size(max = 20, message = "Le code statut ne peut excéder 20 caractères")
    @Column(name = "status_code", length = 20)
    private String statusCode = "CREATED";

    /**
     * Statut du paiement (PENDING, PAID, PARTIALLY_PAID, FAILED, REFUNDED)
     * Doit correspondre à un code existant dans le système
     */
    @Size(max = 20, message = "Le code statut de paiement ne peut excéder 20 caractères")
    @Column(name = "payment_status_code", length = 20)
    private String paymentStatusCode = "PENDING";

    /**
     * Notes internes ou commentaires sur la commande
     * Peut contenir des informations pour le service client
     */
    @Size(max = 2000, message = "Les notes ne peuvent excéder 2000 caractères")
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    /**
     * Date prévue de livraison
     * Doit être dans le futur si spécifiée
     */
    @Future(message = "La date de livraison doit être dans le futur")
    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    /**
     * Date effective de complétion de la commande
     * Null si la commande n'est pas encore terminée
     */
    @PastOrPresent(message = "La date de complétion doit être dans le passé ou présent")
    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    /**
     * Référence vers l'utilisateur ayant passé la commande Relation Many-to-One vers UserModel Ne fait pas partie de la
     * sérialisation JSON
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnore
    private UserModel user;

    /**
     * Référence vers l'adresse de livraison
     * Relation Many-to-One vers UserAddressModel
     * Ne fait pas partie de la sérialisation JSON
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_address_id", insertable = false, updatable = false)
    @JsonIgnore
    private UserAddressModel deliveryAddress;

    /**
     * Liste des articles de la commande
     * Relation One-to-Many vers OrderItemModel
     * Cascade: les opérations sur la commande affectent ses articles
     */
    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<OrderItemModel> items = new ArrayList<>();

    /**
     * Liste des plans de paiement échelonnés associés
     * Relation One-to-Many vers InstallmentPlanModel
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<InstallmentPlanModel> installmentPlans = new ArrayList<>();

    /**
     * Calcule le montant total à partir des articles
     */
    @PreUpdate
    private void calculateTotalAmount () {
        if (items != null) {
            this.totalAmount = items.stream()
                    .map(OrderItemModel::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }

    /**
     * Vérifie si la commande est payée
     *
     * @return true si paymentStatusCode est "PAID", false sinon
     */
    public boolean isPaid () {
        return "PAID".equals(paymentStatusCode);
    }

    /**
     * Vérifie si la commande peut être annulée
     * @return true si le statut est CREATED ou PROCESSING, false sinon
     */
    public boolean isCancellable () {
        return "CREATED".equals(statusCode) || "PROCESSING".equals(statusCode);
    }

    /**
     * Marque la commande comme complétée avec la date/heure actuelle
     */
    public void markAsCompleted () {
        this.statusCode = "DELIVERED";
        this.completedAt = LocalDateTime.now();
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