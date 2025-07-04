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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "categories", schema = "kredika_app")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CategoryModel extends BaseModel {

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "slug")
    private String slug;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "parent_id")
    private UUID parentId;

    @Column(name = "position")
    private Integer position;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "seo_title")
    private String seoTitle;

    @Column(name = "seo_description", columnDefinition = "TEXT")
    private String seoDescription;

    // Relations
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ProductModel> products = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    @JsonIgnore
    private CategoryModel parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CategoryModel> children = new ArrayList<>();

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getDescription () {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    public String getSlug () {
        return slug;
    }

    public void setSlug (String slug) {
        this.slug = slug;
    }

    public String getImageUrl () {
        return imageUrl;
    }

    public void setImageUrl (String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public UUID getParentId () {
        return parentId;
    }

    public void setParentId (UUID parentId) {
        this.parentId = parentId;
    }

    public Integer getPosition () {
        return position;
    }

    public void setPosition (Integer position) {
        this.position = position;
    }

    public Boolean getActive () {
        return isActive;
    }

    public void setActive (Boolean active) {
        isActive = active;
    }

    public String getSeoTitle () {
        return seoTitle;
    }

    public void setSeoTitle (String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public String getSeoDescription () {
        return seoDescription;
    }

    public void setSeoDescription (String seoDescription) {
        this.seoDescription = seoDescription;
    }

    public List<ProductModel> getProducts () {
        return products;
    }

    public void setProducts (List<ProductModel> products) {
        this.products = products;
    }

    public CategoryModel getParent () {
        return parent;
    }

    public void setParent (CategoryModel parent) {
        this.parent = parent;
    }

    public List<CategoryModel> getChildren () {
        return children;
    }

    public void setChildren (List<CategoryModel> children) {
        this.children = children;
    }
}