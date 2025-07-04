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
import java.util.List;

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

    @NotBlank(message = "Le nom complet est requis")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NotBlank(message = "L'email est requis")
    @Email(message = "L'email doit être valide")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Pattern(regexp = "^[+]?[\\d\\s-()]+$", message = "Format de téléphone invalide")
    @Column(name = "phone_number")
    private String phoneNumber;

    @Past(message = "La date de naissance doit être dans le passé")
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "national_id")
    private String nationalId;

    @Size(max = 100, message = "La profession ne peut excéder 100 caractères")
    @Column(name = "profession")
    private String profession;

    @DecimalMin(value = "0.0", inclusive = false, message = "Le revenu mensuel doit être positif")
    @Digits(integer = 10, fraction = 2, message = "Format de revenu invalide")
    @Column(name = "monthly_income", precision = 10, scale = 2)
    private BigDecimal monthlyIncome;

    @Column(name = "role_code")
    private String roleCode = "CUSTOMER";

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Column(name = "email_verified")
    private Boolean emailVerified = false;

    @Column(name = "phone_verified")
    private Boolean phoneVerified = false;

    @Column(name = "keycloak_id")
    private String keycloakId;

    @Column(name = "status_code")
    private String statusCode = "ACTIVE";

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "preferred_language")
    private String preferredLanguage = "fr";

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "roles", columnDefinition = "jsonb")
    private List<String> roles = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "preferences", columnDefinition = "jsonb")
    private Object preferences;

    // Relations
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<OrderModel> orders = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private CreditProfileModel creditProfile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserAddressModel> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CartModel> carts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<InstallmentPlanModel> installmentPlans = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PaymentTransactionModel> paymentTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserSessionModel> userSessions = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<NotificationModel> notifications = new ArrayList<>();

    // Méthodes utilitaires
    public Integer getAge () {
        if (dateOfBirth != null) {
            return Period.between(dateOfBirth, LocalDate.now()).getYears();
        }
        return null;
    }

    public Boolean isFullyVerified () {
        return Boolean.TRUE.equals(emailVerified) && Boolean.TRUE.equals(phoneVerified);
    }

    public UserAddressModel getDefaultAddress () {
        return addresses.stream()
                .filter(addr -> Boolean.TRUE.equals(addr.getDefault()))
                .findFirst()
                .orElse(null);
    }

    public String getDisplayName () {
        return fullName != null ? fullName : email.split("@")[0];
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