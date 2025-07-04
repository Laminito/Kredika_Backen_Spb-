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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @NotBlank(message = "Le nom du produit est requis")
    @Size(min = 2, max = 255, message = "Le nom doit contenir entre 2 et 255 caractères")
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Size(max = 500, message = "La description courte ne peut excéder 500 caractères")
    @Column(name = "short_description", length = 500)
    private String shortDescription;

    @NotNull(message = "Le prix est requis")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être positif")
    @Digits(integer = 10, fraction = 2, message = "Format de prix invalide")
    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @DecimalMin(value = "0.0", message = "Le prix de comparaison doit être positif")
    @Column(name = "compare_price", precision = 10, scale = 2)
    private BigDecimal comparePrice;

    @DecimalMin(value = "0.0", message = "Le coût doit être positif")
    @Column(name = "cost", precision = 10, scale = 2)
    private BigDecimal cost;

    @Column(name = "sku", unique = true)
    private String sku;

    @Min(value = 0, message = "Le stock ne peut être négatif")
    @Column(name = "stock")
    private Integer stock = 0;

    @Min(value = 0, message = "Le stock minimum ne peut être négatif")
    @Column(name = "min_stock")
    private Integer minStock = 0;

    @DecimalMin(value = "0.0", message = "Le poids doit être positif")
    @Column(name = "weight", precision = 8, scale = 2)
    private BigDecimal weight;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "dimensions", columnDefinition = "jsonb")
    private Object dimensions;

    @NotNull(message = "La catégorie est requise")
    @Column(name = "category_id", nullable = false)
    private UUID categoryId;

    @Size(max = 100, message = "La marque ne peut excéder 100 caractères")
    @Column(name = "brand")
    private String brand;

    @Size(max = 100, message = "Le modèle ne peut excéder 100 caractères")
    @Column(name = "model")
    private String model;

    @Size(max = 50, message = "La couleur ne peut excéder 50 caractères")
    @Column(name = "color")
    private String color;

    @Min(value = 0, message = "La garantie ne peut être négative")
    @Column(name = "warranty")
    private Integer warranty;

    @Column(name = "warranty_unit")
    private String warrantyUnit = "MONTHS";

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "credit_eligible")
    private Boolean creditEligible = false;

    @DecimalMin(value = "0.0", message = "Le montant crédit minimum doit être positif")
    @Column(name = "min_credit_amount", precision = 10, scale = 2)
    private BigDecimal minCreditAmount;

    @Min(value = 1, message = "La durée crédit maximale doit être positive")
    @Column(name = "max_credit_duration")
    private Integer maxCreditDuration;

    @Column(name = "rating", precision = 3, scale = 2)
    private BigDecimal rating;

    @Column(name = "review_count")
    private Integer reviewCount = 0;

    @Column(name = "view_count")
    private Long viewCount = 0L;

    @Column(name = "purchase_count")
    private Long purchaseCount = 0L;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "tags", columnDefinition = "jsonb")
    private List<String> tags = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "attributes", columnDefinition = "jsonb")
    private Object attributes;

    @Column(name = "seo_title")
    private String seoTitle;

    @Column(name = "seo_description", columnDefinition = "TEXT")
    private String seoDescription;

    @Column(name = "slug", unique = true)
    private String slug;

    @Column(name = "meta_keywords")
    private String metaKeywords;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    @JsonIgnore
    private CategoryModel category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ProductImageModel> images = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CartItemModel> cartItems = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<OrderItemModel> orderItems = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<InstallmentPlanModel> installmentPlans = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ProductReviewModel> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<WishlistModel> wishlists = new ArrayList<>();

    // Méthodes utilitaires
    public Boolean isInStock () {
        return stock != null && stock > 0;
    }

    public Boolean isLowStock () {
        return stock != null && minStock != null && stock <= minStock;
    }

    public Boolean hasDiscount () {
        return comparePrice != null && comparePrice.compareTo(price) > 0;
    }

    public BigDecimal getDiscountPercentage () {
        if (!hasDiscount()) return BigDecimal.ZERO;
        return comparePrice.subtract(price)
                .multiply(BigDecimal.valueOf(100))
                .divide(comparePrice, 2, BigDecimal.ROUND_HALF_UP);
    }

    public String getMainImageUrl () {
        return images.stream()
                .filter(img -> Boolean.TRUE.equals(img.getIsPrimary()))
                .findFirst()
                .map(ProductImageModel::getImageUrl)
                .orElse(null);
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

