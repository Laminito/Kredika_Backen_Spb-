package sn.kredika_app.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "user_addresses", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserAddressModel extends BaseModel {

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "type_code")
    private String typeCode;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "region")
    private String region;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "country")
    private String country;

    @Column(name = "is_default")
    private Boolean isDefault = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnore
    private UserModel user;

    // Méthode utilitaire pour la représentation texte
    public String getFullAddress () {
        return String.format("%s, %s, %s, %s %s, %s",
                street, city, region, postalCode, country
        );
    }

    public UUID getUserId () {
        return userId;
    }

    public void setUserId (UUID userId) {
        this.userId = userId;
    }

    public String getTypeCode () {
        return typeCode;
    }

    public void setTypeCode (String typeCode) {
        this.typeCode = typeCode;
    }

    public String getStreet () {
        return street;
    }

    public void setStreet (String street) {
        this.street = street;
    }

    public String getCity () {
        return city;
    }

    public void setCity (String city) {
        this.city = city;
    }

    public String getRegion () {
        return region;
    }

    public void setRegion (String region) {
        this.region = region;
    }

    public String getPostalCode () {
        return postalCode;
    }

    public void setPostalCode (String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry () {
        return country;
    }

    public void setCountry (String country) {
        this.country = country;
    }

    public Boolean getDefault () {
        return isDefault;
    }

    public void setDefault (Boolean aDefault) {
        isDefault = aDefault;
    }

    public UserModel getUser () {
        return user;
    }

    public void setUser (UserModel user) {
        this.user = user;
    }
}


