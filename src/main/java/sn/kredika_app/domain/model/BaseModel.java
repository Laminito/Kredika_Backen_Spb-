package sn.kredika_app.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@MappedSuperclass
@Where(clause = "is_deleted = false")
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Version
    @Column(name = "version")
    private Long version;

    @PreRemove
    public void preRemove () {
        this.isDeleted = true;
    }

    @PrePersist
    public void prePersist () {
        if (this.isDeleted == null) {
            this.isDeleted = false;
        }
    }

    public UUID getId () {
        return id;
    }

    public void setId (UUID id) {
        this.id = id;
    }

    public Boolean getDeleted () {
        return isDeleted;
    }

    public void setDeleted (Boolean deleted) {
        isDeleted = deleted;
    }

    public LocalDateTime getCreatedAt () {
        return createdAt;
    }

    public void setCreatedAt (LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt () {
        return updatedAt;
    }

    public void setUpdatedAt (LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy () {
        return createdBy;
    }

    public void setCreatedBy (String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy () {
        return lastModifiedBy;
    }

    public void setLastModifiedBy (String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Long getVersion () {
        return version;
    }

    public void setVersion (Long version) {
        this.version = version;
    }
}
