package sn.kredika_app.domain.model.vehicle;

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
@Table(name = "vehicle_documents", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VehicleDocumentModel extends BaseModel {

    /**
     * Type de document (ex: carte_grise, assurance, visite_technique).
     */
    @NotBlank
    @Column(name = "document_type")
    private String documentType;

    /**
     * URL ou chemin du fichier du document.
     */
    @Column(name = "file_url")
    private String fileUrl;

    /**
     * Date de validité du document (ex: date d’expiration d’une assurance).
     */
    private LocalDate validUntil;

    /**
     * Véhicule auquel le document est rattaché.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    @JsonIgnore
    private VehicleModel vehicle;

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

    public VehicleModel getVehicle () {
        return vehicle;
    }

    public void setVehicle (VehicleModel vehicle) {
        this.vehicle = vehicle;
    }
}
