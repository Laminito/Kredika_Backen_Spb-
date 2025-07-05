package sn.kredika_app.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.util.UUID;


/**
 * Représente une image associée à un produit dans le catalogue. Permet de gérer les visuels multiples pour un même
 * produit.
 */
@Entity
@Table(name = "product_images", schema = "kredika_app")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ProductImageModel extends BaseModel {

    /**
     * Identifiant du produit associé Clé étrangère vers ProductModel
     */
    @NotNull(message = "L'identifiant du produit est obligatoire")
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    /**
     * URL complète de l'image stockée Doit pointer vers une ressource valide
     */
    @NotBlank(message = "L'URL de l'image est obligatoire")
    @URL(message = "L'URL de l'image doit être valide")
    @Size(max = 512, message = "L'URL de l'image ne peut excéder 512 caractères")
    @Column(name = "image_url", nullable = false, length = 512)
    private String imageUrl;

    /**
     * Texte alternatif pour l'accessibilité (SEO) Décrit le contenu de l'image
     */
    @Size(max = 100, message = "Le texte alternatif ne peut excéder 100 caractères")
    @Column(name = "alt_text", length = 100)
    private String altText;

    /**
     * Position dans le carrousel d'images Détermine l'ordre d'affichage (plus petit = plus prioritaire)
     */
    @Min(value = 0, message = "La position ne peut être négative")
    @Column(name = "position")
    private Integer position = 0;

    /**
     * Indique si c'est l'image principale du produit Une seule image par produit peut être marquée comme principale
     * Valeur par défaut: false
     */
    @Column(name = "is_primary")
    private Boolean isPrimary = false;

    /**
     * Référence vers le produit associé Relation Many-to-One vers ProductModel Ne fait pas partie de la sérialisation
     * JSON
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    @JsonIgnore
    private ProductModel product;

    /**
     * Vérifie si l'image est dans un format supporté
     *
     * @return true si l'URL se termine par .jpg, .png, .jpeg ou .webp
     */
    public boolean isSupportedFormat () {
        return imageUrl != null &&
                (imageUrl.toLowerCase().endsWith(".jpg") ||
                        imageUrl.toLowerCase().endsWith(".png") ||
                        imageUrl.toLowerCase().endsWith(".jpeg") ||
                        imageUrl.toLowerCase().endsWith(".webp"));
    }

    /**
     * Génère un texte alternatif par défaut si non spécifié
     *
     * @return "Image du produit [productName]" si altText est vide
     */
    public String getEffectiveAltText () {
        if (altText != null && !altText.isBlank()) {
            return altText;
        }
        return product != null ?
                "Image du produit " + product.getName() :
                "Image de produit";
    }

    public Boolean getIsPrimary () {
        return isPrimary;
    }

    public void setIsPrimary (Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public String getImageUrl () {
        return imageUrl;
    }

    public void setImageUrl (String imageUrl) {
        this.imageUrl = imageUrl;
    }

}