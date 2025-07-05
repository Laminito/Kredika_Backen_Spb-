package sn.kredika_app.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Modèle représentant un utilisateur dans le système.
 */
@Entity
@Table(
        name = "users", schema = "kredika_app",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email"),
                @Index(name = "idx_user_phone", columnList = "phone_number"),
                @Index(name = "idx_user_keycloak_id", columnList = "keycloak_id")
        }
)
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class UserModel extends BaseModel {

    /**
     * Nom complet de l'utilisateur. Doit contenir entre 2 et 100 caractères. Ce champ est obligatoire.
     */
    @NotBlank(message = "Le nom complet est requis")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    /**
     * Adresse email de l'utilisateur. Doit être unique et valide. Ce champ est obligatoire.
     */
    @NotBlank(message = "L'email est requis")
    @Email(message = "L'email doit être valide")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * Numéro de téléphone de l'utilisateur.
     * Doit respecter le format international.
     */
    @Pattern(regexp = "^\\+(?:[0-9] ?){6,14}[0-9]$", message = "Format de téléphone international invalide (ex: +33 6 12 34 56 78)")
    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    /**
     * Date de naissance de l'utilisateur.
     * Doit être dans le passé.
     */
    @Past(message = "La date de naissance doit être dans le passé")
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    /**
     * Numéro d'identification national (CNI, passeport, etc.).
     */
    @Size(min = 5, max = 50, message = "L'identifiant national doit contenir entre 5 et 50 caractères")
    @Column(name = "national_id", unique = true)
    private String nationalId;

    /**
     * Profession de l'utilisateur.
     * Limitée à 100 caractères maximum.
     */
    @Size(max = 100, message = "La profession ne peut excéder 100 caractères")
    @Column(name = "profession")
    private String profession;

    /**
     * Revenu mensuel de l'utilisateur.
     * Doit être positif si renseigné.
     */
    @DecimalMin(value = "0.0", inclusive = false, message = "Le revenu mensuel doit être positif")
    @Digits(integer = 10, fraction = 2, message = "Format de revenu invalide")
    @Column(name = "monthly_income", precision = 10, scale = 2)
    private BigDecimal monthlyIncome;

    /**
     * Code du rôle principal de l'utilisateur.
     * Par défaut à "CUSTOMER".
     */
    @Column(name = "role_code")
    private String roleCode = "CUSTOMER";

    /**
     * Indique si le compte utilisateur est vérifié.
     * Par défaut à false.
     */
    @Column(name = "is_verified")
    private Boolean isVerified = false;

    /**
     * Indique si l'email a été vérifié.
     * Par défaut à false.
     */
    @Column(name = "email_verified")
    private Boolean emailVerified = false;

    /**
     * Indique si le téléphone a été vérifié.
     * Par défaut à false.
     */
    @Column(name = "phone_verified")
    private Boolean phoneVerified = false;

    /**
     * Identifiant Keycloak pour l'authentification.
     */
    @Column(name = "keycloak_id", unique = true)
    private String keycloakId;

    /**
     * Statut du compte utilisateur (ACTIVE, SUSPENDED, etc.).
     * Par défaut à "ACTIVE".
     */
    @Column(name = "status_code")
    private String statusCode = "ACTIVE";

    /**
     * URL de l'image de profil.
     */
    @Column(name = "profile_image_url")
    private String profileImageUrl;

    /**
     * Langue préférée de l'utilisateur.
     * Par défaut à "fr".
     */
    @Column(name = "preferred_language", length = 2)
    private String preferredLanguage = "fr";

    /**
     * Date et heure de la dernière connexion.
     */
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    /**
     * Liste des rôles de l'utilisateur.
     * Stockée sous forme JSON dans la base de données.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "roles", columnDefinition = "jsonb")
    private List<String> roles = new ArrayList<>();

    /**
     * Préférences utilisateur personnalisées.
     * Stockées sous forme JSON dans la base de données.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "preferences", columnDefinition = "jsonb")
    private Object preferences;

    /**
     * Commandes passées par cet utilisateur.
     * Relation OneToMany chargée en mode LAZY.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<OrderModel> orders = new ArrayList<>();

    /**
     * Profil de crédit associé à cet utilisateur.
     * Relation OneToOne chargée en mode LAZY.
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private CreditProfileModel creditProfile;

    /**
     * Adresses de cet utilisateur.
     * Relation OneToMany chargée en mode LAZY.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserAddressModel> addresses = new ArrayList<>();

    /**
     * Paniers de cet utilisateur.
     * Relation OneToMany chargée en mode LAZY.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CartModel> carts = new ArrayList<>();

    /**
     * Plans de paiement associés à cet utilisateur.
     * Relation OneToMany chargée en mode LAZY.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<InstallmentPlanModel> installmentPlans = new ArrayList<>();

    /**
     * Transactions de paiement de cet utilisateur.
     * Relation OneToMany chargée en mode LAZY.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PaymentTransactionModel> paymentTransactions = new ArrayList<>();

    /**
     * Sessions de cet utilisateur.
     * Relation OneToMany chargée en mode LAZY.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserSessionModel> userSessions = new ArrayList<>();

    /**
     * Notifications reçues par cet utilisateur.
     * Relation OneToMany chargée en mode LAZY.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<NotificationModel> notifications = new ArrayList<>();

    /**
     * Calcule l'âge de l'utilisateur en années.
     * @return l'âge en années ou null si la date de naissance n'est pas renseignée
     */
    public Integer getAge () {
        if (dateOfBirth == null) return null;
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    /**
     * Vérifie si l'utilisateur est majeur.
     *
     * @return true si l'utilisateur a 18 ans ou plus, false sinon
     */
    public Boolean isAdult () {
        Integer age = getAge();
        return age != null && age >= 18;
    }

    /**
     * Vérifie si l'utilisateur est complètement vérifié (email et téléphone).
     * @return true si l'utilisateur est vérifié, false sinon
     */
    public Boolean isFullyVerified () {
        return Boolean.TRUE.equals(emailVerified) && Boolean.TRUE.equals(phoneVerified);
    }

    /**
     * Récupère l'adresse par défaut de l'utilisateur.
     * @return l'adresse par défaut ou null si aucune n'est définie
     */
    public UserAddressModel getDefaultAddress () {
        if (addresses == null) return null;
        return addresses.stream()
                .filter(addr -> Boolean.TRUE.equals(addr.getDefault()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Génère un nom d'affichage pour l'utilisateur.
     * @return le nom complet si disponible, sinon la partie avant @ de l'email
     */
    public String getDisplayName () {
        if (fullName != null && !fullName.trim().isEmpty()) {
            return fullName;
        }
        return email != null ? email.split("@")[0] : "";
    }

    /**
     * Vérifie si l'utilisateur a un rôle spécifique.
     *
     * @param role le rôle à vérifier
     * @return true si l'utilisateur a le rôle, false sinon
     */
    public boolean hasRole (String role) {
        return roles != null && roles.contains(role);
    }

    /**
     * Ajoute un rôle à l'utilisateur.
     *
     * @param role le rôle à ajouter
     */
    public void addRole (String role) {
        if (role != null && !role.trim().isEmpty()) {
            if (roles == null) {
                roles = new ArrayList<>();
            }
            if (!roles.contains(role)) {
                roles.add(role);
            }
        }
    }

    /**
     * Met à jour la date de dernière connexion.
     */
    public void updateLastLogin () {
        this.lastLoginAt = LocalDateTime.now();
    }

    /**
     * Vérifie si le compte utilisateur est actif.
     *
     * @return true si le statut est ACTIVE, false sinon
     */
    public boolean isActive () {
        return "ACTIVE".equalsIgnoreCase(statusCode);
    }

    /**
     * Vérifie si l'utilisateur a un profil de crédit.
     *
     * @return true si un profil de crédit existe, false sinon
     */
    public boolean hasCreditProfile () {
        return creditProfile != null;
    }

    /**
     * Récupère le panier actif de l'utilisateur.
     *
     * @return le panier actif ou null si aucun n'est trouvé
     */
    public CartModel getActiveCart () {
        if (carts == null) return null;
        return carts.stream()
                .filter(cart -> "ACTIVE".equals(cart.getStatusCode()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Génère les initiales de l'utilisateur.
     *
     * @return les initiales (ex: "JD" pour "John Doe")
     */
    public String getInitials () {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "";
        }
        String[] names = fullName.split(" ");
        if (names.length == 0) return "";
        if (names.length == 1) return names[0].substring(0, 1).toUpperCase();
        return (names[0].charAt(0) + names[names.length - 1].substring(0, 1)).toUpperCase();
    }

    /**
     * Vérifie si l'utilisateur a une adresse email en domaine professionnel.
     *
     * @return true si l'email est professionnel, false sinon
     */
    public boolean hasProfessionalEmail () {
        if (email == null) return false;
        String[] domains = {"gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "live.com"};
        String domain = email.split("@")[1].toLowerCase();
        return Arrays.stream(domains).noneMatch(d -> d.equalsIgnoreCase(domain));
    }


    public @NotBlank(message = "Le nom complet est requis") @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères") String getFullName () {
        return fullName;
    }

    public void setFullName (@NotBlank(message = "Le nom complet est requis") @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères") String fullName) {
        this.fullName = fullName;
    }

    public @NotBlank(message = "L'email est requis") @Email(message = "L'email doit être valide") String getEmail () {
        return email;
    }

    public void setEmail (@NotBlank(message = "L'email est requis") @Email(message = "L'email doit être valide") String email) {
        this.email = email;
    }

    public @Pattern(regexp = "^[+]?[\\d\\s-()]+$", message = "Format de téléphone invalide") String getPhoneNumber () {
        return phoneNumber;
    }

    public void setPhoneNumber (@Pattern(regexp = "^[+]?[\\d\\s-()]+$", message = "Format de téléphone invalide") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @Past(message = "La date de naissance doit être dans le passé") LocalDate getDateOfBirth () {
        return dateOfBirth;
    }

    public void setDateOfBirth (@Past(message = "La date de naissance doit être dans le passé") LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationalId () {
        return nationalId;
    }

    public void setNationalId (String nationalId) {
        this.nationalId = nationalId;
    }

    public @Size(max = 100, message = "La profession ne peut excéder 100 caractères") String getProfession () {
        return profession;
    }

    public void setProfession (@Size(max = 100, message = "La profession ne peut excéder 100 caractères") String profession) {
        this.profession = profession;
    }

    public @DecimalMin(value = "0.0", inclusive = false, message = "Le revenu mensuel doit être positif") @Digits(integer = 10, fraction = 2, message = "Format de revenu invalide") BigDecimal getMonthlyIncome () {
        return monthlyIncome;
    }

    public void setMonthlyIncome (@DecimalMin(value = "0.0", inclusive = false, message = "Le revenu mensuel doit être positif") @Digits(integer = 10, fraction = 2, message = "Format de revenu invalide") BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public String getRoleCode () {
        return roleCode;
    }

    public void setRoleCode (String roleCode) {
        this.roleCode = roleCode;
    }

    public Boolean getVerified () {
        return isVerified;
    }

    public void setVerified (Boolean verified) {
        isVerified = verified;
    }

    public Boolean getEmailVerified () {
        return emailVerified;
    }

    public void setEmailVerified (Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Boolean getPhoneVerified () {
        return phoneVerified;
    }

    public void setPhoneVerified (Boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    public String getKeycloakId () {
        return keycloakId;
    }

    public void setKeycloakId (String keycloakId) {
        this.keycloakId = keycloakId;
    }

    public String getStatusCode () {
        return statusCode;
    }

    public void setStatusCode (String statusCode) {
        this.statusCode = statusCode;
    }

    public String getProfileImageUrl () {
        return profileImageUrl;
    }

    public void setProfileImageUrl (String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getPreferredLanguage () {
        return preferredLanguage;
    }

    public void setPreferredLanguage (String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public LocalDateTime getLastLoginAt () {
        return lastLoginAt;
    }

    public void setLastLoginAt (LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public List<String> getRoles () {
        return roles;
    }

    public void setRoles (List<String> roles) {
        this.roles = roles;
    }

    public Object getPreferences () {
        return preferences;
    }

    public void setPreferences (Object preferences) {
        this.preferences = preferences;
    }

    public List<OrderModel> getOrders () {
        return orders;
    }

    public void setOrders (List<OrderModel> orders) {
        this.orders = orders;
    }

    public CreditProfileModel getCreditProfile () {
        return creditProfile;
    }

    public void setCreditProfile (CreditProfileModel creditProfile) {
        this.creditProfile = creditProfile;
    }

    public List<UserAddressModel> getAddresses () {
        return addresses;
    }

    public void setAddresses (List<UserAddressModel> addresses) {
        this.addresses = addresses;
    }

    public List<CartModel> getCarts () {
        return carts;
    }

    public void setCarts (List<CartModel> carts) {
        this.carts = carts;
    }

    public List<InstallmentPlanModel> getInstallmentPlans () {
        return installmentPlans;
    }

    public void setInstallmentPlans (List<InstallmentPlanModel> installmentPlans) {
        this.installmentPlans = installmentPlans;
    }

    public List<PaymentTransactionModel> getPaymentTransactions () {
        return paymentTransactions;
    }

    public void setPaymentTransactions (List<PaymentTransactionModel> paymentTransactions) {
        this.paymentTransactions = paymentTransactions;
    }

    public List<UserSessionModel> getUserSessions () {
        return userSessions;
    }

    public void setUserSessions (List<UserSessionModel> userSessions) {
        this.userSessions = userSessions;
    }

    public List<NotificationModel> getNotifications () {
        return notifications;
    }

    public void setNotifications (List<NotificationModel> notifications) {
        this.notifications = notifications;
    }
}