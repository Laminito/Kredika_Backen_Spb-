package sn.kredika_app.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NotificationModel extends BaseModel {

    @NotNull(message = "L'utilisateur est requis")
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @NotBlank(message = "Le titre est requis")
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "type")
    private String type;

    @Column(name = "priority")
    private String priority = "NORMAL";

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "channel")
    private String channel = "APP";

    @Column(name = "action_url")
    private String actionUrl;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "data", columnDefinition = "jsonb")
    private Object data;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnore
    private UserModel user;

    // Méthodes utilitaires
    public void markAsRead () {
        this.isRead = true;
        this.readAt = LocalDateTime.now();
    }

    public @NotNull(message = "L'utilisateur est requis") UUID getUserId () {
        return userId;
    }

    public void setUserId (@NotNull(message = "L'utilisateur est requis") UUID userId) {
        this.userId = userId;
    }

    public @NotBlank(message = "Le titre est requis") String getTitle () {
        return title;
    }

    public void setTitle (@NotBlank(message = "Le titre est requis") String title) {
        this.title = title;
    }

    public String getMessage () {
        return message;
    }

    public void setMessage (String message) {
        this.message = message;
    }

    public String getType () {
        return type;
    }

    public void setType (String type) {
        this.type = type;
    }

    public String getPriority () {
        return priority;
    }

    public void setPriority (String priority) {
        this.priority = priority;
    }

    public Boolean getRead () {
        return isRead;
    }

    public void setRead (Boolean read) {
        isRead = read;
    }

    public LocalDateTime getReadAt () {
        return readAt;
    }

    public void setReadAt (LocalDateTime readAt) {
        this.readAt = readAt;
    }

    public String getChannel () {
        return channel;
    }

    public void setChannel (String channel) {
        this.channel = channel;
    }

    public String getActionUrl () {
        return actionUrl;
    }

    public void setActionUrl (String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public Object getData () {
        return data;
    }

    public void setData (Object data) {
        this.data = data;
    }

    public LocalDateTime getSentAt () {
        return sentAt;
    }

    public void setSentAt (LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public UserModel getUser () {
        return user;
    }

    public void setUser (UserModel user) {
        this.user = user;
    }
}