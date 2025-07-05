package sn.kredika_app.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import sn.kredika_app.domain.dto.persistence.MetadataPersistenceDto;


/**
 * Représente une liste de codes standards (référentiel) pour gérer les données métier normalisées comme les statuts,
 * types, catégories, etc. Permet une gestion dynamique des valeurs possibles dans le système.
 */
@Entity
@Table(
        name = "code_lists",
        schema = "kredika_app",
        indexes = {
                @Index(name = "idx_code_list_type", columnList = "type"),
                @Index(name = "idx_code_list_code", columnList = "code"),
                @Index(name = "idx_code_list_type_code", columnList = "type, code")
        }
)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CodeListModel extends BaseModel {

    /**
     * Type de la liste de codes (ex: "ORDER_STATUS", "PAYMENT_METHOD") Doit être en majuscules et sans espaces
     */
    @NotBlank(message = "Le type de liste de codes est obligatoire")
    @Size(max = 50, message = "Le type ne peut excéder 50 caractères")
    @Pattern(regexp = "^[A-Z_]+$", message = "Le type doit être en MAJUSCULES avec des underscores")
    @Column(name = "type", nullable = false, length = 50)
    private String type;

    /**
     * Code unique identifiant la valeur dans son type
     * Doit être en majuscules et sans espaces
     */
    @NotBlank(message = "Le code est obligatoire")
    @Size(max = 30, message = "Le code ne peut excéder 30 caractères")
    @Pattern(regexp = "^[A-Z0-9_]+$", message = "Le code doit être en MAJUSCULES avec chiffres/underscores")
    @Column(name = "code", nullable = false, length = 30)
    private String code;

    /**
     * Valeur associée au code (peut être une valeur d'affichage ou technique)
     */
    @Size(max = 100, message = "La valeur ne peut excéder 100 caractères")
    @Column(name = "value", length = 100)
    private String value;

    /**
     * Libellé d'affichage convivial pour l'utilisateur
     */
    @Size(max = 100, message = "Le libellé ne peut excéder 100 caractères")
    @Column(name = "label", length = 100)
    private String label;

    /**
     * Description détaillée du code (usage, règles métier)
     */
    @Size(max = 500, message = "La description ne peut excéder 500 caractères")
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Indique si le code est actif et utilisable dans le système
     * Valeur par défaut: true
     */
    @Column(name = "is_active")
    private Boolean isActive = true;

    /**
     * Position dans les listes déroulantes (pour le tri)
     * Plus la valeur est petite, plus le code apparaît en haut
     */
    @Min(value = 0, message = "La position ne peut être négative")
    @Column(name = "position")
    private Integer position;

    /**
     * Métadonnées supplémentaires au format JSON
     * Peut contenir des configurations spécifiques au code
     * Ex: { "color": "#FF0000", "icon": "check-circle" }
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private MetadataPersistenceDto metadata;

    /**
     * Vérifie si le code est inactif
     *
     * @return true si isActive == false, false sinon
     */
    public boolean isInactive () {
        return !Boolean.TRUE.equals(isActive);
    }

    /**
     * Génère une clé unique combinant type et code
     *
     * @return La clé sous forme "TYPE:CODE"
     */
    public String getCompositeKey () {
        return type + ":" + code;
    }

    public @NotBlank(message = "Le type est requis") String getType () {
        return type;
    }

    public void setType (@NotBlank(message = "Le type est requis") String type) {
        this.type = type;
    }

    public @NotBlank(message = "Le code est requis") String getCode () {
        return code;
    }

    public void setCode (@NotBlank(message = "Le code est requis") String code) {
        this.code = code;
    }

    public String getValue () {
        return value;
    }

    public void setValue (String value) {
        this.value = value;
    }

    public String getLabel () {
        return label;
    }

    public void setLabel (String label) {
        this.label = label;
    }

    public String getDescription () {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    public Boolean getActive () {
        return isActive;
    }

    public void setActive (Boolean active) {
        isActive = active;
    }

    public Integer getPosition () {
        return position;
    }

    public void setPosition (Integer position) {
        this.position = position;
    }

    public MetadataPersistenceDto getMetadata () {
        return metadata;
    }

    public void setMetadata (MetadataPersistenceDto metadata) {
        this.metadata = metadata;
    }
}