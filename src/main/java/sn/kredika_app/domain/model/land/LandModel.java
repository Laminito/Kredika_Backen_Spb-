package sn.kredika_app.domain.model.land;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import sn.kredika_app.domain.model.BaseModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "lands", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LandModel extends BaseModel {

    /**
     * Localisation du terrain (ex: adresse complète ou repère).
     */
    private String location;

    /**
     * Type juridique du terrain : TF, Bail, Délibération, etc.
     */
    private String landType;

    /**
     * Superficie en mètres carrés.
     */
    private Double area;

    /**
     * Référence cadastrale ou numéro du titre foncier.
     */
    private String reference;

    /**
     * Date d’acquisition du terrain par son propriétaire.
     */
    private LocalDate acquisitionDate;

    /**
     * Latitude du terrain (ex: 14.6937).
     */
    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;

    /**
     * Longitude du terrain (ex: -17.4441).
     */
    @Column(precision = 11, scale = 8)
    private BigDecimal longitude;

    /**
     * ID du propriétaire lié au terrain.
     */
    private UUID ownerId;

    /**
     * Liste des documents associés à ce terrain.
     */
    @OneToMany(mappedBy = "land", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LandDocumentModel> documents = new ArrayList<>();

    public String getLocation () {
        return location;
    }

    public void setLocation (String location) {
        this.location = location;
    }

    public String getLandType () {
        return landType;
    }

    public void setLandType (String landType) {
        this.landType = landType;
    }

    public Double getArea () {
        return area;
    }

    public void setArea (Double area) {
        this.area = area;
    }

    public String getReference () {
        return reference;
    }

    public void setReference (String reference) {
        this.reference = reference;
    }

    public LocalDate getAcquisitionDate () {
        return acquisitionDate;
    }

    public void setAcquisitionDate (LocalDate acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public BigDecimal getLatitude () {
        return latitude;
    }

    public void setLatitude (BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude () {
        return longitude;
    }

    public void setLongitude (BigDecimal longitude) {
        this.longitude = longitude;
    }

    public UUID getOwnerId () {
        return ownerId;
    }

    public void setOwnerId (UUID ownerId) {
        this.ownerId = ownerId;
    }

    public List<LandDocumentModel> getDocuments () {
        return documents;
    }

    public void setDocuments (List<LandDocumentModel> documents) {
        this.documents = documents;
    }
}
