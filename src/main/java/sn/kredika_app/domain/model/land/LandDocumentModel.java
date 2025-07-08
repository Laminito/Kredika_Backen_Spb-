package sn.kredika_app.domain.model.land;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import sn.kredika_app.domain.model.BaseModel;

import java.time.LocalDate;

@Entity
@Table(name = "land_documents", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LandDocumentModel extends BaseModel {

    /**
     * Type de document (ex: TF, bail, délibération, plan cadastral).
     */
    @NotBlank
    @Column(name = "document_type")
    private String documentType;

    /**
     * URL ou chemin vers le fichier du document.
     */
    @Column(name = "file_url")
    private String fileUrl;

    /**
     * Date de validité du document (optionnelle selon le type).
     */
    private LocalDate validUntil;

    /**
     * Terrain auquel le document est lié.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "land_id")
    @JsonIgnore
    private LandModel land;

    public @NotBlank String getDocumentType () {
        return documentType;
    }

    public void setDocumentType (@NotBlank String documentType) {
        this.documentType = documentType;
    }

    public String getFileUrl () {
        return fileUrl;
    }

    public void setFileUrl (String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public LocalDate getValidUntil () {
        return validUntil;
    }

    public void setValidUntil (LocalDate validUntil) {
        this.validUntil = validUntil;
    }

    public LandModel getLand () {
        return land;
    }

    public void setLand (LandModel land) {
        this.land = land;
    }
}
