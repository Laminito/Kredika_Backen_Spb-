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
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import sn.kredika_app.domain.dto.persistence.NotificationDataPersistenceDto;

import java.time.LocalDateTime;
import java.util.UUID;


/**
 * Représente une notification envoyée à un utilisateur dans le système. Peut être utilisée pour des alertes, messages
 * système ou notifications métier.
 */
@Entity
@Table(name = "notifications", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NotificationModel extends BaseModel {

    /**
     * Identifiant de l'utilisateur destinataire Obligatoire - Clé étrangère vers UserModel
     */
    @NotNull(message = "L'identifiant utilisateur est obligatoire")
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    /**
     * Titre de la notification (affiché en gras) Doit être concis et informatif
     */
    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 2, max = 100, message = "Le titre doit contenir entre 2 et 100 caractères")
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    /**
     * Message détaillé de la notification
     * Peut contenir du HTML/markdown pour le formatage
     */
    @Size(max = 2000, message = "Le message ne peut excéder 2000 caractères")
    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    /**
     * Type de notification (ex: "SYSTEM", "ORDER", "PAYMENT", "ALERT")
     * Utilisé pour le routage et le style d'affichage
     */
    @Size(max = 30, message = "Le type ne peut excéder 30 caractères")
    @Column(name = "type", length = 30)
    private String type;

    /**
     * Priorité de la notification (LOW, NORMAL, HIGH, URGENT)
     * Valeur par défaut: NORMAL
     * Détermine l'ordre d'affichage et les badges visuels
     */
    @Column(name = "priority", length = 10)
    private String priority = "NORMAL";

    /**
     * Indique si la notification a été lue par l'utilisateur
     * Valeur par défaut: false
     */
    @Column(name = "is_read")
    private Boolean isRead = false;

    /**
     * Date/heure de lecture par l'utilisateur
     * Null si la notification n'a pas encore été lue
     */
    @PastOrPresent(message = "La date de lecture doit être dans le passé ou présent")
    @Column(name = "read_at")
    private LocalDateTime readAt;

    /**
     * Canal de diffusion (APP, EMAIL, SMS, PUSH)
     * Valeur par défaut: APP
     */
    @Column(name = "channel", length = 10)
    private String channel = "APP";

    /**
     * URL d'action associée à la notification
     * Permet de rediriger l'utilisateur vers une page spécifique
     */
    @Size(max = 500, message = "L'URL d'action ne peut excéder 500 caractères")
    @Column(name = "action_url", length = 500)
    private String actionUrl;

    /**
     * Données supplémentaires au format JSON
     * Peut contenir des métadonnées spécifiques au type de notification
     * Ex: {"orderId": "12345", "amount": "5000"}
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "data", columnDefinition = "jsonb")
    private NotificationDataPersistenceDto data;

    /**
     * Date/heure d'envoi de la notification
     * Null si la notification n'a pas encore été envoyée
     */
    @PastOrPresent(message = "La date d'envoi doit être dans le passé ou présent")
    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    // ===== RELATIONS =====

    /**
     * Référence vers l'utilisateur destinataire Relation Many-to-One vers UserModel Ne fait pas partie de la
     * sérialisation JSON
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnore
    private UserModel user;

    /**
     * Marque la notification comme lue avec la date/heure actuelle
     */
    public void markAsRead () {
        this.isRead = true;
        this.readAt = LocalDateTime.now();
    }

    /**
     * Vérifie si la notification est de haute priorité
     *
     * @return true si priority est HIGH ou URGENT, false sinon
     */
    public boolean isHighPriority () {
        return "HIGH".equals(priority) || "URGENT".equals(priority);
    }

    /**
     * Vérifie si la notification a été envoyée
     *
     * @return true si sentAt n'est pas null, false sinon
     */
    public boolean isSent () {
        return sentAt != null;
    }

    /**
     * Vérifie si la notification est expirée (non lue après 30 jours)
     *
     * @return true si la notification n'est pas lue et a plus de 30 jours
     */
    public boolean isExpired () {
        return !isRead && sentAt != null
                && sentAt.plusDays(30).isBefore(LocalDateTime.now());
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

    public NotificationDataPersistenceDto getData () {
        return data;
    }

    public void setData (NotificationDataPersistenceDto data) {
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