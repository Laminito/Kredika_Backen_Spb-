package sn.kredika_app.domain.model.vehicle;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import sn.kredika_app.domain.model.BaseModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "vehicles", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VehicleModel extends BaseModel {

    /**
     * Numéro d'immatriculation officiel du véhicule (ex: DK-1234-AA).
     */
    @NotBlank
    @Column(name = "registration_number", nullable = false, unique = true)
    private String registrationNumber;

    /**
     * Type de véhicule (ex: moto, voiture, camion...).
     */
    @Column(name = "type")
    private String type;

    /**
     * Marque du véhicule (ex: Toyota, Yamaha).
     */
    private String brand;

    /**
     * Modèle spécifique du véhicule (ex: Corolla 2020).
     */
    private String model;

    /**
     * Numéro de châssis (VIN), identifiant unique du véhicule.
     */
    @Column(name = "chassis_number")
    private String chassisNumber;

    /**
     * Couleur du véhicule.
     */
    private String color;

    /**
     * Date d'acquisition par le propriétaire actuel.
     */
    private LocalDate acquisitionDate;

    /**
     * ID du propriétaire dans la base des utilisateurs.
     */
    private UUID ownerId;

    /**
     * Documents liés au véhicule : carte grise, assurance, etc.
     */
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehicleDocumentModel> documents = new ArrayList<>();

    public @NotBlank String getRegistrationNumber () {
        return registrationNumber;
    }

    public void setRegistrationNumber (@NotBlank String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getType () {
        return type;
    }

    public void setType (String type) {
        this.type = type;
    }

    public String getBrand () {
        return brand;
    }

    public void setBrand (String brand) {
        this.brand = brand;
    }

    public String getModel () {
        return model;
    }

    public void setModel (String model) {
        this.model = model;
    }

    public String getChassisNumber () {
        return chassisNumber;
    }

    public void setChassisNumber (String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public String getColor () {
        return color;
    }

    public void setColor (String color) {
        this.color = color;
    }

    public LocalDate getAcquisitionDate () {
        return acquisitionDate;
    }

    public void setAcquisitionDate (LocalDate acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public UUID getOwnerId () {
        return ownerId;
    }

    public void setOwnerId (UUID ownerId) {
        this.ownerId = ownerId;
    }

    public List<VehicleDocumentModel> getDocuments () {
        return documents;
    }

    public void setDocuments (List<VehicleDocumentModel> documents) {
        this.documents = documents;
    }
}
