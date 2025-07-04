package sn.kredika_app.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(
        name = "code_lists", schema = "kredika_app",
        indexes = {
                @Index(name = "idx_code_list_type", columnList = "type"),
                @Index(name = "idx_code_list_code", columnList = "code"),
                @Index(name = "idx_code_list_type_code", columnList = "type, code")
        }
)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CodeListModel extends BaseModel {

    @NotBlank(message = "Le type est requis")
    @Column(name = "type", nullable = false)
    private String type;

    @NotBlank(message = "Le code est requis")
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "value")
    private String value;

    @Column(name = "label")
    private String label;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "position")
    private Integer position;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private Object metadata;

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

    public Object getMetadata () {
        return metadata;
    }

    public void setMetadata (Object metadata) {
        this.metadata = metadata;
    }
}