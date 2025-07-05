package sn.kredika_app.domain.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Modèle représentant une session utilisateur dans le système. Stocke les informations d'authentification et d'activité
 * des utilisateurs.
 */
@Entity
@Table(name = "user_sessions", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserSessionModel extends BaseModel {

    /**
     * Identifiant de l'utilisateur associé à cette session. Ce champ est obligatoire.
     */
    @NotNull(message = "L'utilisateur est requis")
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    /**
     * Token unique identifiant la session. Généré lors de la création de la session.
     */
    @NotBlank(message = "Le token de session est requis")
    @Size(min = 64, max = 256, message = "Le token de session doit contenir entre 64 et 256 caractères")
    @Column(name = "session_token", unique = true, nullable = false, length = 256)
    private String sessionToken;

    /**
     * Informations sur le dispositif utilisé (mobile, desktop, etc.).
     * Stockées sous forme JSON.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "device_info", columnDefinition = "jsonb")
    private Object deviceInfo;

    /**
     * Adresse IP de l'utilisateur lors de la création de la session.
     * Format IPv4 ou IPv6.
     */
    @Pattern(
            regexp = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$|^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$",
            message = "Format d'adresse IP invalide"
    )
    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    /**
     * User-Agent du navigateur ou de l'application cliente.
     */
    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    /**
     * Date et heure d'expiration de la session.
     * Après cette date, la session n'est plus valide.
     */
    @Future(message = "La date d'expiration doit être dans le futur")
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    /**
     * Date et heure de la dernière activité enregistrée.
     * Mise à jour à chaque interaction avec la session.
     */
    @Column(name = "last_activity")
    private LocalDateTime lastActivity;

    /**
     * Indique si la session est active.
     * Par défaut à true.
     */
    @Column(name = "is_active")
    private Boolean isActive = true;

    /**
     * Utilisateur associé à cette session.
     * Relation ManyToOne chargée en mode LAZY.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnore
    private UserModel user;

    /**
     * Vérifie si la session est expirée.
     * @return true si la session est expirée, false sinon
     */
    public Boolean isExpired () {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    /**
     * Vérifie si la session est valide (active et non expirée).
     *
     * @return true si la session est valide, false sinon
     */
    public Boolean isValid () {
        return Boolean.TRUE.equals(isActive) && !isExpired();
    }

    /**
     * Met à jour la date de dernière activité à maintenant.
     */
    public void updateLastActivity () {
        this.lastActivity = LocalDateTime.now();
    }

    /**
     * Invalide la session (la marque comme inactive).
     */
    public void invalidate () {
        this.isActive = false;
    }

    /**
     * Prolonge la durée de la session.
     *
     * @param durationMinutes nombre de minutes à ajouter
     */
    public void extendSession (int durationMinutes) {
        if (durationMinutes > 0) {
            this.expiresAt = this.expiresAt.plusMinutes(durationMinutes);
        }
    }

    /**
     * Récupère le temps restant avant expiration en minutes.
     *
     * @return nombre de minutes restantes ou null si non défini
     */
    public Long getRemainingTimeMinutes () {
        if (expiresAt == null) return null;
        return java.time.Duration.between(LocalDateTime.now(), expiresAt).toMinutes();
    }

    /**
     * Vérifie si la session est inactive depuis trop longtemps.
     *
     * @param maxInactiveMinutes durée maximale d'inactivité en minutes
     * @return true si la session est inactive depuis trop longtemps
     */
    public Boolean isInactiveTooLong (int maxInactiveMinutes) {
        if (lastActivity == null) return true;
        return java.time.Duration.between(lastActivity, LocalDateTime.now()).toMinutes() > maxInactiveMinutes;
    }

    /**
     * Récupère une représentation simplifiée du dispositif.
     *
     * @return le type de dispositif ou "Inconnu"
     */
    public String getDeviceType () {
        if (userAgent == null) return "Inconnu";
        if (userAgent.contains("Mobile")) return "Mobile";
        if (userAgent.contains("Tablet")) return "Tablette";
        if (userAgent.contains("Windows") || userAgent.contains("Macintosh") || userAgent.contains("Linux")) {
            return "Ordinateur";
        }
        return "Autre";
    }

    /**
     * Vérifie si la session provient d'un nouveau dispositif.
     *
     * @param knownDevices liste des dispositifs connus de l'utilisateur
     * @return true si c'est un nouveau dispositif
     */
    public Boolean isNewDevice (List<String> knownDevices) {
        if (userAgent == null || knownDevices == null) return false;
        return knownDevices.stream().noneMatch(device -> device.equals(userAgent));
    }

    public @NotNull(message = "L'utilisateur est requis") UUID getUserId () {
        return userId;
    }

    public void setUserId (@NotNull(message = "L'utilisateur est requis") UUID userId) {
        this.userId = userId;
    }

    public String getSessionToken () {
        return sessionToken;
    }

    public void setSessionToken (String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public void setDeviceInfo (String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getIpAddress () {
        return ipAddress;
    }

    public void setIpAddress (String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent () {
        return userAgent;
    }

    public void setUserAgent (String userAgent) {
        this.userAgent = userAgent;
    }

    public LocalDateTime getExpiresAt () {
        return expiresAt;
    }

    public void setExpiresAt (LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getLastActivity () {
        return lastActivity;
    }

    public void setLastActivity (LocalDateTime lastActivity) {
        this.lastActivity = lastActivity;
    }

    public Boolean getActive () {
        return isActive;
    }

    public void setActive (Boolean active) {
        isActive = active;
    }

    public UserModel getUser () {
        return user;
    }

    public void setUser (UserModel user) {
        this.user = user;
    }
}