package sn.kredika_app.domain.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_sessions", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserSessionModel extends BaseModel {

    @NotNull(message = "L'utilisateur est requis")
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "session_token", unique = true)
    private String sessionToken;

    @Column(name = "device_info")
    private String deviceInfo;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "last_activity")
    private LocalDateTime lastActivity;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnore
    private UserModel user;

    public Boolean isExpired () {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
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

    public String getDeviceInfo () {
        return deviceInfo;
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