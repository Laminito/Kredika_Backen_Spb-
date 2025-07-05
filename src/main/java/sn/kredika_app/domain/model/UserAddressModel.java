package sn.kredika_app.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

/**
 * Modèle représentant une adresse utilisateur dans le système.
 */
@Entity
@Table(name = "user_addresses", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserAddressModel extends BaseModel {

    /**
     * Identifiant de l'utilisateur associé à cette adresse. Ce champ est obligatoire.
     */
    @NotNull(message = "L'utilisateur est requis")
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    /**
     * Type d'adresse (MAISON, TRAVAIL, etc.). Utilise un code prédéfini dans le système.
     */
    @NotBlank(message = "Le type d'adresse est requis")
    @Size(max = 20, message = "Le type d'adresse ne peut excéder 20 caractères")
    @Column(name = "type_code", nullable = false, length = 20)
    private String typeCode;

    /**
     * Rue et numéro de l'adresse. Ce champ est obligatoire.
     */
    @NotBlank(message = "La rue est requise")
    @Size(max = 255, message = "La rue ne peut excéder 255 caractères")
    @Column(name = "street", nullable = false)
    private String street;

    /**
     * Ville de l'adresse. Ce champ est obligatoire.
     */
    @NotBlank(message = "La ville est requise")
    @Size(max = 100, message = "La ville ne peut excéder 100 caractères")
    @Column(name = "city", nullable = false)
    private String city;

    /**
     * Région/État/Province de l'adresse.
     */
    @Size(max = 100, message = "La région ne peut excéder 100 caractères")
    @Column(name = "region")
    private String region;

    /**
     * Code postal de l'adresse. Ce champ est obligatoire.
     */
    @NotBlank(message = "Le code postal est requis")
    @Size(max = 20, message = "Le code postal ne peut excéder 20 caractères")
    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    /**
     * Pays de l'adresse. Ce champ est obligatoire. Doit utiliser un code pays ISO standard.
     */
    @NotBlank(message = "Le pays est requis")
    @Size(min = 2, max = 2, message = "Le code pays doit avoir 2 caractères (ISO code)")
    @Column(name = "country", nullable = false, length = 2)
    private String country;

    /**
     * Indique si c'est l'adresse par défaut de l'utilisateur. Par défaut à false.
     */
    @Column(name = "is_default")
    private Boolean isDefault = false;

    /**
     * Utilisateur associé à cette adresse. Relation ManyToOne chargée en mode LAZY.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnore
    private UserModel user;

    @DecimalMin(value = "-90.0", message = "La latitude doit être >= -90")
    @DecimalMax(value = "90.0", message = "La latitude doit être <= 90")
    @Column(name = "latitude", precision = 9, scale = 6)
    private Double latitude;

    @DecimalMin(value = "-180.0", message = "La longitude doit être >= -180")
    @DecimalMax(value = "180.0", message = "La longitude doit être <= 180")
    @Column(name = "longitude", precision = 9, scale = 6)
    private Double longitude;


    /**
     * Convertit l'adresse en coordonnées GPS via une API (ex: OpenStreetMap Nominatim).
     */
    public void geocodeAddress () throws IOException, InterruptedException {
        String fullAddress = String.format("%s, %s, %s", street, city, country);
        String url = "https://nominatim.openstreetmap.org/search?format=json&q="
                + URLEncoder.encode(fullAddress, StandardCharsets.UTF_8);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "KredikaApp/1.0")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200 && response.body().length() > 2) {
            String lat = response.body().split("\"lat\":\"")[1].split("\"")[0];
            String lon = response.body().split("\"lon\":\"")[1].split("\"")[0];
            this.latitude = Double.parseDouble(lat);
            this.longitude = Double.parseDouble(lon);
        }
    }

    /**
     * Calcule la distance en km entre cette adresse et une autre.
     */
    public double calculateDistanceTo (UserAddressModel other) {
        if (this.latitude == null || other.latitude == null) return -1;

        double earthRadius = 6371; // Rayon de la Terre en km
        double dLat = Math.toRadians(other.latitude - this.latitude);
        double dLng = Math.toRadians(other.longitude - this.longitude);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(this.latitude))
                * Math.cos(Math.toRadians(other.latitude))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }

    /**
     * Retourne l'adresse complète formatée.
     *
     * @return la chaîne formatée contenant l'adresse complète
     */
    public String getFullAddress () {
        return String.format(
                "%s, %s%s, %s %s, %s",
                street,
                city,
                (region != null && !region.isEmpty()) ? ", " + region : "",
                postalCode,
                country
        ).replaceAll(", , ", ", "); // Nettoyage des éléments vides
    }

    /**
     * Définit cette adresse comme adresse par défaut. Désactive les autres adresses par défaut si nécessaire (à gérer
     * au niveau service).
     */
    public void setAsDefault () {
        this.isDefault = true;
    }

    /**
     * Vérifie si l'adresse est valide (contient tous les champs obligatoires).
     *
     * @return true si l'adresse est valide, false sinon
     */
    public boolean isValid () {
        return userId != null &&
                typeCode != null && !typeCode.isEmpty() &&
                street != null && !street.isEmpty() &&
                city != null && !city.isEmpty() &&
                postalCode != null && !postalCode.isEmpty() &&
                country != null && country.length() == 2;
    }

    /**
     * Compare cette adresse avec une autre pour détecter si elles sont similaires.
     *
     * @param other l'autre adresse à comparer
     * @return true si les adresses sont similaires, false sinon
     */
    public boolean isSimilarTo (UserAddressModel other) {
        if (other == null) return false;

        return Objects.equals(this.street, other.street) &&
                Objects.equals(this.city, other.city) &&
                Objects.equals(this.region, other.region) &&
                Objects.equals(this.postalCode, other.postalCode) &&
                Objects.equals(this.country, other.country);
    }

    /**
     * Retourne une version abrégée de l'adresse (ville et pays).
     *
     * @return la chaîne formatée contenant la ville et le pays
     */
    public String getShortAddress () {
        return String.format("%s, %s", city, country);
    }

    /**
     * Retourne le nom du type d'adresse formaté.
     *
     * @return le type formaté (ex: "Maison" au lieu de "HOME")
     */
    public String getFormattedType () {
        if (typeCode == null) return "";

        return typeCode.charAt(0) + typeCode.substring(1).toLowerCase();
    }

    /**
     * Vérifie si l'adresse est une adresse internationale.
     *
     * @return true si le pays n'est pas le pays par défaut (à adapter)
     */
    public boolean isInternational () {
        // À adapter selon votre pays par défaut
        return !"FR".equalsIgnoreCase(country);
    }

    /**
     * Clone les informations d'adresse (sans l'ID et les relations).
     *
     * @return un nouveau modèle avec les mêmes informations d'adresse
     */
    public UserAddressModel cloneAddress () {
        UserAddressModel clone = new UserAddressModel();
        clone.setUserId(this.userId);
        clone.setTypeCode(this.typeCode);
        clone.setStreet(this.street);
        clone.setCity(this.city);
        clone.setRegion(this.region);
        clone.setPostalCode(this.postalCode);
        clone.setCountry(this.country);
        clone.setDefault(this.isDefault);
        return clone;
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

    public @DecimalMin(value = "-90.0", message = "La latitude doit être >= -90") @DecimalMax(value = "90.0", message = "La latitude doit être <= 90") Double getLatitude () {
        return latitude;
    }

    public void setLatitude (@DecimalMin(value = "-90.0", message = "La latitude doit être >= -90") @DecimalMax(value = "90.0", message = "La latitude doit être <= 90") Double latitude) {
        this.latitude = latitude;
    }

    public @DecimalMin(value = "-180.0", message = "La longitude doit être >= -180") @DecimalMax(value = "180.0", message = "La longitude doit être <= 180") Double getLongitude () {
        return longitude;
    }

    public void setLongitude (@DecimalMin(value = "-180.0", message = "La longitude doit être >= -180") @DecimalMax(value = "180.0", message = "La longitude doit être <= 180") Double longitude) {
        this.longitude = longitude;
    }
}


