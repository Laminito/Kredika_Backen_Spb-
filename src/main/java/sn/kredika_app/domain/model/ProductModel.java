package sn.kredika_app.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Modèle représentant un produit dans le système.
 */
@Entity
@Table(
        name = "products", schema = "kredika_app",
        indexes = {
                @Index(name = "idx_product_category", columnList = "category_id"),
                @Index(name = "idx_product_sku", columnList = "sku"),
                @Index(name = "idx_product_slug", columnList = "slug"),
                @Index(name = "idx_product_active", columnList = "is_active"),
                @Index(name = "idx_product_featured", columnList = "is_featured")
        }
)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductModel extends BaseModel {

    /**
     * Nom du produit (2 à 255 caractères). Ce champ est obligatoire.
     */
    @NotBlank(message = "Le nom du produit est requis")
    @Size(min = 2, max = 255, message = "Le nom doit contenir entre 2 et 255 caractères")
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Description détaillée du produit. Stockée sous forme de texte dans la base de données.
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Description courte du produit (500 caractères max).
     * Utilisée pour les aperçus et les listes de produits.
     */
    @Size(max = 500, message = "La description courte ne peut excéder 500 caractères")
    @Column(name = "short_description", length = 500)
    private String shortDescription;

    /**
     * Prix courant du produit.
     * Doit être positif et avoir un format valide (10 chiffres avant la virgule, 2 après).
     * Ce champ est obligatoire.
     */
    @NotNull(message = "Le prix est requis")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être positif")
    @Digits(integer = 10, fraction = 2, message = "Format de prix invalide")
    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    /**
     * Prix de comparaison (ancien prix barré).
     * Doit être positif si renseigné.
     */
    @DecimalMin(value = "0.0", message = "Le prix de comparaison doit être positif")
    @Column(name = "compare_price", precision = 10, scale = 2)
    private BigDecimal comparePrice;

    /**
     * Coût d'achat du produit pour l'entreprise.
     * Doit être positif si renseigné.
     */
    @DecimalMin(value = "0.0", message = "Le coût doit être positif")
    @Column(name = "cost", precision = 10, scale = 2)
    private BigDecimal cost;

    /**
     * SKU (Stock Keeping Unit) - identifiant unique du produit.
     */
    @Column(name = "sku", unique = true)
    private String sku;

    /**
     * Quantité en stock.
     * Par défaut à 0, ne peut être négative.
     */
    @Min(value = 0, message = "Le stock ne peut être négatif")
    @Column(name = "stock")
    private Integer stock = 0;

    /**
     * Niveau de stock minimum avant alerte.
     * Par défaut à 0, ne peut être négative.
     */
    @Min(value = 0, message = "Le stock minimum ne peut être négatif")
    @Column(name = "min_stock")
    private Integer minStock = 0;

    /**
     * Poids du produit en kg.
     * Doit être positif si renseigné.
     */
    @DecimalMin(value = "0.0", message = "Le poids doit être positif")
    @Column(name = "weight", precision = 8, scale = 2)
    private BigDecimal weight;

    /**
     * Dimensions du produit (longueur, largeur, hauteur).
     * Stockées sous forme JSON dans la base de données.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "dimensions", columnDefinition = "jsonb")
    private Object dimensions;

    /**
     * Identifiant de la catégorie du produit.
     * Ce champ est obligatoire.
     */
    @NotNull(message = "La catégorie est requise")
    @Column(name = "category_id", nullable = false)
    private UUID categoryId;

    /**
     * Marque du produit (100 caractères max).
     */
    @Size(max = 100, message = "La marque ne peut excéder 100 caractères")
    @Column(name = "brand")
    private String brand;

    /**
     * Modèle du produit (100 caractères max).
     */
    @Size(max = 100, message = "Le modèle ne peut excéder 100 caractères")
    @Column(name = "model")
    private String model;

    /**
     * Couleur du produit (50 caractères max).
     */
    @Size(max = 50, message = "La couleur ne peut excéder 50 caractères")
    @Column(name = "color")
    private String color;

    /**
     * Durée de garantie (en mois par défaut).
     * Ne peut être négative.
     */
    @Min(value = 0, message = "La garantie ne peut être négative")
    @Column(name = "warranty")
    private Integer warranty;

    /**
     * Unité de temps pour la garantie (JOURS, MOIS, ANNÉES).
     * Par défaut à "MOIS".
     */
    @Column(name = "warranty_unit")
    private String warrantyUnit = "MONTHS";

    /**
     * Indique si le produit est en vedette.
     * Par défaut à false.
     */
    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    /**
     * Indique si le produit est actif (visible en boutique).
     * Par défaut à true.
     */
    @Column(name = "is_active")
    private Boolean isActive = true;

    /**
     * Indique si le produit est éligible au paiement en plusieurs fois.
     * Par défaut à false.
     */
    @Column(name = "credit_eligible")
    private Boolean creditEligible = false;

    /**
     * Montant minimum pour l'option de crédit.
     * Doit être positif si renseigné.
     */
    @DecimalMin(value = "0.0", message = "Le montant crédit minimum doit être positif")
    @Column(name = "min_credit_amount", precision = 10, scale = 2)
    private BigDecimal minCreditAmount;

    /**
     * Durée maximum de crédit en mois.
     * Doit être positive si renseignée.
     */
    @Min(value = 1, message = "La durée crédit maximale doit être positive")
    @Column(name = "max_credit_duration")
    private Integer maxCreditDuration;

    /**
     * Note moyenne du produit (sur 5).
     * Stockée avec 2 décimales.
     */
    @Column(name = "rating", precision = 3, scale = 2)
    private BigDecimal rating;

    /**
     * Nombre total d'avis sur le produit.
     * Par défaut à 0.
     */
    @Column(name = "review_count")
    private Integer reviewCount = 0;

    /**
     * Nombre total de vues du produit.
     * Par défaut à 0.
     */
    @Column(name = "view_count")
    private Long viewCount = 0L;

    /**
     * Nombre total d'achats du produit.
     * Par défaut à 0.
     */
    @Column(name = "purchase_count")
    private Long purchaseCount = 0L;

    /**
     * Tags associés au produit.
     * Stockés sous forme JSON dans la base de données.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "tags", columnDefinition = "jsonb")
    private List<String> tags = new ArrayList<>();

    /**
     * Attributs spécifiques du produit (caractéristiques techniques).
     * Stockés sous forme JSON dans la base de données.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "attributes", columnDefinition = "jsonb")
    private Object attributes;

    /**
     * Titre SEO personnalisé.
     */
    @Column(name = "seo_title")
    private String seoTitle;

    /**
     * Description SEO personnalisée.
     * Stockée sous forme de texte dans la base de données.
     */
    @Column(name = "seo_description", columnDefinition = "TEXT")
    private String seoDescription;

    /**
     * Slug pour les URLs SEO.
     * Doit être unique.
     */
    @Column(name = "slug", unique = true)
    private String slug;

    /**
     * Mots-clés pour le référencement.
     */
    @Column(name = "meta_keywords")
    private String metaKeywords;

    /**
     * Catégorie associée au produit.
     * Relation ManyToOne chargée en mode LAZY.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    @JsonIgnore
    private CategoryModel category;

    /**
     * Images associées au produit.
     * Relation OneToMany chargée en mode LAZY.
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ProductImageModel> images = new ArrayList<>();

    /**
     * Items de panier contenant ce produit.
     * Relation OneToMany chargée en mode LAZY.
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CartItemModel> cartItems = new ArrayList<>();

    /**
     * Items de commande contenant ce produit.
     * Relation OneToMany chargée en mode LAZY.
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<OrderItemModel> orderItems = new ArrayList<>();

    /**
     * Plans de paiement disponibles pour ce produit.
     * Relation OneToMany chargée en mode LAZY.
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<InstallmentPlanModel> installmentPlans = new ArrayList<>();

    /**
     * Avis sur ce produit.
     * Relation OneToMany chargée en mode LAZY.
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ProductReviewModel> reviews = new ArrayList<>();

    /**
     * Listes de souhaits contenant ce produit.
     * Relation OneToMany chargée en mode LAZY.
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<WishlistModel> wishlists = new ArrayList<>();

    // ======================
    // Méthodes utilitaires
    // ======================

    /**
     * Vérifie si le produit est en stock.
     *
     * @return true si le stock est disponible, false sinon
     */
    public Boolean isInStock () {
        return stock != null && stock > 0;
    }

    /**
     * Vérifie si le produit est en stock critique (niveau minimum atteint).
     *
     * @return true si le stock est inférieur ou égal au stock minimum, false sinon
     */
    public Boolean isLowStock () {
        return stock != null && minStock != null && stock <= minStock;
    }

    /**
     * Vérifie si le produit a une réduction (prix de comparaison > prix courant).
     *
     * @return true si une réduction est active, false sinon
     */
    public Boolean hasDiscount () {
        return comparePrice != null && comparePrice.compareTo(price) > 0;
    }

    /**
     * Calcule le pourcentage de réduction.
     *
     * @return le pourcentage de réduction ou 0 si pas de réduction
     */
    public BigDecimal getDiscountPercentage () {
        if (!hasDiscount()) return BigDecimal.ZERO;
        return comparePrice.subtract(price)
                .multiply(BigDecimal.valueOf(100))
                .divide(comparePrice, 2, RoundingMode.HALF_UP);
    }

    /**
     * Récupère l'URL de l'image principale du produit.
     *
     * @return l'URL de l'image principale ou null si aucune image n'est définie comme principale
     */
    public String getMainImageUrl () {
        return images.stream()
                .filter(img -> Boolean.TRUE.equals(img.getIsPrimary()))
                .findFirst()
                .map(ProductImageModel::getImageUrl)
                .orElse(null);
    }

    /**
     * Récupère toutes les URLs d'images du produit.
     *
     * @return liste des URLs d'images
     */
    public List<String> getAllImageUrls () {
        return images.stream()
                .sorted(Comparator.comparing(ProductImageModel::getPosition))
                .map(ProductImageModel::getImageUrl)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour la note moyenne du produit en fonction des avis.
     *
     * @param newRating nouvelle note à intégrer dans le calcul
     * @param isNew     true si c'est un nouvel avis, false si c'est une mise à jour
     */
    public void updateRating (BigDecimal newRating, boolean isNew) {
        if (newRating == null) return;

        BigDecimal total = rating != null ? rating.multiply(BigDecimal.valueOf(reviewCount)) : BigDecimal.ZERO;

        if (isNew) {
            total = total.add(newRating);
            reviewCount++;
        } else {
            // Pour une mise à jour, on suppose qu'on remplace l'ancienne note
            // (la logique exacte dépendra de notre approche d'implémentation)
            total = total.add(newRating); // Simplification
        }

        this.rating = total.divide(BigDecimal.valueOf(reviewCount), 2, RoundingMode.HALF_UP);
    }

    /**
     * Vérifie si le produit est nouveau (moins de 30 jours depuis sa création).
     *
     * @return true si le produit est considéré comme nouveau, false sinon
     */
    public Boolean isNewProduct () {
        return getCreatedAt() != null &&
                getCreatedAt().plusDays(30).isAfter(LocalDateTime.now());
    }

    /**
     * Incrémente le compteur de vues du produit.
     */
    public void incrementViewCount () {
        this.viewCount = (this.viewCount == null) ? 1 : this.viewCount + 1;
    }

    /**
     * Incrémente le compteur d'achats du produit.
     *
     * @param quantity quantité achetée à ajouter
     */
    public void incrementPurchaseCount (int quantity) {
        this.purchaseCount = (this.purchaseCount == null) ? quantity : this.purchaseCount + quantity;
    }

    /**
     * Ajoute un tag au produit.
     *
     * @param tag tag à ajouter
     */
    public void addTag (String tag) {
        if (tag != null && !tag.trim().isEmpty()) {
            if (this.tags == null) {
                this.tags = new ArrayList<>();
            }
            this.tags.add(tag.trim());
        }
    }

    /**
     * Vérifie si le produit a un tag spécifique.
     *
     * @param tag tag à vérifier
     * @return true si le tag est présent, false sinon
     */
    public boolean hasTag (String tag) {
        return this.tags != null && this.tags.contains(tag);
    }

    /**
     * Calcule la disponibilité du stock.
     *
     * @return "En stock" si disponible, "Stock faible" si niveau critique, "Rupture" sinon
     */
    public String getStockStatus () {
        if (isInStock()) {
            return isLowStock() ? "Stock faible" : "En stock";
        }
        return "Rupture";
    }


    public @NotBlank(message = "Le nom du produit est requis") @Size(min = 2, max = 255, message = "Le nom doit contenir entre 2 et 255 caractères") String getName () {
        return name;
    }

    public void setName (@NotBlank(message = "Le nom du produit est requis") @Size(min = 2, max = 255, message = "Le nom doit contenir entre 2 et 255 caractères") String name) {
        this.name = name;
    }

    public String getDescription () {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    public @Size(max = 500, message = "La description courte ne peut excéder 500 caractères") String getShortDescription () {
        return shortDescription;
    }

    public void setShortDescription (@Size(max = 500, message = "La description courte ne peut excéder 500 caractères") String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public @NotNull(message = "Le prix est requis") @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être positif") @Digits(integer = 10, fraction = 2, message = "Format de prix invalide") BigDecimal getPrice () {
        return price;
    }

    public void setPrice (@NotNull(message = "Le prix est requis") @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être positif") @Digits(integer = 10, fraction = 2, message = "Format de prix invalide") BigDecimal price) {
        this.price = price;
    }

    public @DecimalMin(value = "0.0", message = "Le prix de comparaison doit être positif") BigDecimal getComparePrice () {
        return comparePrice;
    }

    public void setComparePrice (@DecimalMin(value = "0.0", message = "Le prix de comparaison doit être positif") BigDecimal comparePrice) {
        this.comparePrice = comparePrice;
    }

    public @DecimalMin(value = "0.0", message = "Le coût doit être positif") BigDecimal getCost () {
        return cost;
    }

    public void setCost (@DecimalMin(value = "0.0", message = "Le coût doit être positif") BigDecimal cost) {
        this.cost = cost;
    }

    public String getSku () {
        return sku;
    }

    public void setSku (String sku) {
        this.sku = sku;
    }

    public @Min(value = 0, message = "Le stock ne peut être négatif") Integer getStock () {
        return stock;
    }

    public void setStock (@Min(value = 0, message = "Le stock ne peut être négatif") Integer stock) {
        this.stock = stock;
    }

    public @Min(value = 0, message = "Le stock minimum ne peut être négatif") Integer getMinStock () {
        return minStock;
    }

    public void setMinStock (@Min(value = 0, message = "Le stock minimum ne peut être négatif") Integer minStock) {
        this.minStock = minStock;
    }

    public @DecimalMin(value = "0.0", message = "Le poids doit être positif") BigDecimal getWeight () {
        return weight;
    }

    public void setWeight (@DecimalMin(value = "0.0", message = "Le poids doit être positif") BigDecimal weight) {
        this.weight = weight;
    }

    public Object getDimensions () {
        return dimensions;
    }

    public void setDimensions (Object dimensions) {
        this.dimensions = dimensions;
    }

    public @NotNull(message = "La catégorie est requise") UUID getCategoryId () {
        return categoryId;
    }

    public void setCategoryId (@NotNull(message = "La catégorie est requise") UUID categoryId) {
        this.categoryId = categoryId;
    }

    public @Size(max = 100, message = "La marque ne peut excéder 100 caractères") String getBrand () {
        return brand;
    }

    public void setBrand (@Size(max = 100, message = "La marque ne peut excéder 100 caractères") String brand) {
        this.brand = brand;
    }

    public @Size(max = 100, message = "Le modèle ne peut excéder 100 caractères") String getModel () {
        return model;
    }

    public void setModel (@Size(max = 100, message = "Le modèle ne peut excéder 100 caractères") String model) {
        this.model = model;
    }

    public @Size(max = 50, message = "La couleur ne peut excéder 50 caractères") String getColor () {
        return color;
    }

    public void setColor (@Size(max = 50, message = "La couleur ne peut excéder 50 caractères") String color) {
        this.color = color;
    }

    public @Min(value = 0, message = "La garantie ne peut être négative") Integer getWarranty () {
        return warranty;
    }

    public void setWarranty (@Min(value = 0, message = "La garantie ne peut être négative") Integer warranty) {
        this.warranty = warranty;
    }

    public String getWarrantyUnit () {
        return warrantyUnit;
    }

    public void setWarrantyUnit (String warrantyUnit) {
        this.warrantyUnit = warrantyUnit;
    }

    public Boolean getFeatured () {
        return isFeatured;
    }

    public void setFeatured (Boolean featured) {
        isFeatured = featured;
    }

    public Boolean getActive () {
        return isActive;
    }

    public void setActive (Boolean active) {
        isActive = active;
    }

    public Boolean getCreditEligible () {
        return creditEligible;
    }

    public void setCreditEligible (Boolean creditEligible) {
        this.creditEligible = creditEligible;
    }

    public @DecimalMin(value = "0.0", message = "Le montant crédit minimum doit être positif") BigDecimal getMinCreditAmount () {
        return minCreditAmount;
    }

    public void setMinCreditAmount (@DecimalMin(value = "0.0", message = "Le montant crédit minimum doit être positif") BigDecimal minCreditAmount) {
        this.minCreditAmount = minCreditAmount;
    }

    public @Min(value = 1, message = "La durée crédit maximale doit être positive") Integer getMaxCreditDuration () {
        return maxCreditDuration;
    }

    public void setMaxCreditDuration (@Min(value = 1, message = "La durée crédit maximale doit être positive") Integer maxCreditDuration) {
        this.maxCreditDuration = maxCreditDuration;
    }

    public BigDecimal getRating () {
        return rating;
    }

    public void setRating (BigDecimal rating) {
        this.rating = rating;
    }

    public Integer getReviewCount () {
        return reviewCount;
    }

    public void setReviewCount (Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Long getViewCount () {
        return viewCount;
    }

    public void setViewCount (Long viewCount) {
        this.viewCount = viewCount;
    }

    public Long getPurchaseCount () {
        return purchaseCount;
    }

    public void setPurchaseCount (Long purchaseCount) {
        this.purchaseCount = purchaseCount;
    }

    public List<String> getTags () {
        return tags;
    }

    public void setTags (List<String> tags) {
        this.tags = tags;
    }

    public Object getAttributes () {
        return attributes;
    }

    public void setAttributes (Object attributes) {
        this.attributes = attributes;
    }

    public String getSeoTitle () {
        return seoTitle;
    }

    public void setSeoTitle (String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public String getSeoDescription () {
        return seoDescription;
    }

    public void setSeoDescription (String seoDescription) {
        this.seoDescription = seoDescription;
    }

    public String getSlug () {
        return slug;
    }

    public void setSlug (String slug) {
        this.slug = slug;
    }

    public String getMetaKeywords () {
        return metaKeywords;
    }

    public void setMetaKeywords (String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    public CategoryModel getCategory () {
        return category;
    }

    public void setCategory (CategoryModel category) {
        this.category = category;
    }

    public List<ProductImageModel> getImages () {
        return images;
    }

    public void setImages (List<ProductImageModel> images) {
        this.images = images;
    }

    public List<CartItemModel> getCartItems () {
        return cartItems;
    }

    public void setCartItems (List<CartItemModel> cartItems) {
        this.cartItems = cartItems;
    }

    public List<OrderItemModel> getOrderItems () {
        return orderItems;
    }

    public void setOrderItems (List<OrderItemModel> orderItems) {
        this.orderItems = orderItems;
    }

    public List<InstallmentPlanModel> getInstallmentPlans () {
        return installmentPlans;
    }

    public void setInstallmentPlans (List<InstallmentPlanModel> installmentPlans) {
        this.installmentPlans = installmentPlans;
    }

    public List<ProductReviewModel> getReviews () {
        return reviews;
    }

    public void setReviews (List<ProductReviewModel> reviews) {
        this.reviews = reviews;
    }

    public List<WishlistModel> getWishlists () {
        return wishlists;
    }

    public void setWishlists (List<WishlistModel> wishlists) {
        this.wishlists = wishlists;
    }

    @Override
    public void prePersist () {
        super.prePersist();
        if (this.viewCount == null) {
            this.viewCount = 0L;
        }
        if (this.purchaseCount == null) {
            this.purchaseCount = 0L;
        }
        if (this.reviewCount == null) {
            this.reviewCount = 0;
        }
    }
}

