package sn.kredika_app.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "carts", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CartModel extends BaseModel {

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "status_code")
    private String statusCode;

    @NotNull(message = "Total amount is required")
    @Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnore
    private UserModel user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CartItemModel> items = new ArrayList<>();

    public UUID getUserId () {
        return userId;
    }

    public void setUserId (UUID userId) {
        this.userId = userId;
    }

    public String getStatusCode () {
        return statusCode;
    }

    public void setStatusCode (String statusCode) {
        this.statusCode = statusCode;
    }

    public @NotNull(message = "Total amount is required") BigDecimal getTotalAmount () {
        return totalAmount;
    }

    public void setTotalAmount (@NotNull(message = "Total amount is required") BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getExpiresAt () {
        return expiresAt;
    }

    public void setExpiresAt (LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public UserModel getUser () {
        return user;
    }

    public void setUser (UserModel user) {
        this.user = user;
    }

    public List<CartItemModel> getItems () {
        return items;
    }

    public void setItems (List<CartItemModel> items) {
        this.items = items;
    }
}