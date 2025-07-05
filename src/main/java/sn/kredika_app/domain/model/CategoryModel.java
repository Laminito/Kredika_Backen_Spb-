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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Représente une catégorie de produits dans le système.
 * Supporte une hiérarchie parent/enfant pour l'organisation des catégories.
 */
@Entity
@Table(name = "categories", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CategoryModel extends BaseModel {

    /**
     * Nom de la catégorie (ex: "Électronique", "Mode Femme")
     * Doit être unique dans le système
     */
    @NotBlank(message = "Le nom de la catégorie est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * Description détaillée de la catégorie
     * Peut contenir du HTML/markdown pour le formatage
     */
    @Size(max = 2000, message = "La description ne peut excéder 2000 caractères")
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Slug URL-friendly généré à partir du nom
     * Ex: "electronique" pour "Électronique"
     */
    @NotBlank(message = "Le slug est obligatoire")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Le slug ne peut contenir que des lettres minuscules, chiffres et tirets")
    @Column(name = "slug", nullable = false, unique = true, length = 120)
    private String slug;

    /**
     * URL de l'image représentant la catégorie
     * Doit pointer vers une image valide
     */
    @URL(message = "L'URL de l'image doit être valide")
    @Column(name = "image_url", length = 512)
    private String imageUrl;

    /**
     * ID de la catégorie parente (pour les sous-catégories)
     * Null si c'est une catégorie racine
     */
    @Column(name = "parent_id")
    private UUID parentId;

    /**
     * Position dans l'affichage (pour le tri manuel)
     * Plus la valeur est petite, plus la catégorie apparaît en haut
     */
    @Min(value = 0, message = "La position ne peut être négative")
    @Column(name = "position")
    private Integer position = 0;

    /**
     * Indique si la catégorie est active et visible aux clients
     * Valeur par défaut: true
     */
    @Column(name = "is_active")
    private Boolean isActive = true;

    /**
     * Titre SEO personnalisé (pour le référencement)
     * Si null, utilise le nom de la catégorie
     */
    @Size(max = 70, message = "Le titre SEO ne peut excéder 70 caractères")
    @Column(name = "seo_title", length = 70)
    private String seoTitle;

    /**
     * Description SEO personnalisée (pour le référencement)
     * Si null, utilise le début de la description
     */
    @Size(max = 160, message = "La description SEO ne peut excéder 160 caractères")
    @Column(name = "seo_description", columnDefinition = "TEXT")
    private String seoDescription;

    // ===== RELATIONS =====

    /**
     * Liste des produits appartenant à cette catégorie
     * Relation One-to-Many bidirectionnelle avec ProductModel
     */
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ProductModel> products = new ArrayList<>();

    /**
     * Catégorie parente (pour les sous-catégories)
     * Relation Many-to-One bidirectionnelle avec CategoryModel
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    @JsonIgnore
    private CategoryModel parent;

    /**
     * Sous-catégories (pour les catégories parentes)
     * Relation One-to-Many bidirectionnelle avec CategoryModel
     */
    @OneToMany(
            mappedBy = "parent",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<CategoryModel> children = new ArrayList<>();

    /**
     * Vérifie si la catégorie est une catégorie racine (sans parent)
     * @return true si c'est une catégorie racine, false sinon
     */
    public boolean isRootCategory() {
        return parentId == null;
    }

    /**
     * Vérifie si la catégorie a des sous-catégories
     * @return true s'il y a des sous-catégories, false sinon
     */
    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }

    /**
     * Compte le nombre de produits actifs dans cette catégorie
     * @return Le nombre de produits actifs
     */
    public long countActiveProducts() {
        return products.stream()
                .filter(ProductModel::getActive)
                .count();
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getDescription () {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    public String getSlug () {
        return slug;
    }

    public void setSlug (String slug) {
        this.slug = slug;
    }

    public String getImageUrl () {
        return imageUrl;
    }

    public void setImageUrl (String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public UUID getParentId () {
        return parentId;
    }

    public void setParentId (UUID parentId) {
        this.parentId = parentId;
    }

    public Integer getPosition () {
        return position;
    }

    public void setPosition (Integer position) {
        this.position = position;
    }

    public Boolean getActive () {
        return isActive;
    }

    public void setActive (Boolean active) {
        isActive = active;
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

    public List<ProductModel> getProducts () {
        return products;
    }

    public void setProducts (List<ProductModel> products) {
        this.products = products;
    }

    public CategoryModel getParent () {
        return parent;
    }

    public void setParent (CategoryModel parent) {
        this.parent = parent;
    }

    public List<CategoryModel> getChildren () {
        return children;
    }

    public void setChildren (List<CategoryModel> children) {
        this.children = children;
    }
}