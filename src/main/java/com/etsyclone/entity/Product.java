package com.etsyclone.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.google.common.base.Objects;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product", indexes = {
        @Index(name = "idx_product_name", columnList = "name"),
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(100)", updatable = false)
    private String name;

    @Lob
    @Column(name = "description", nullable = false, columnDefinition = "TEXT", length = 1000)
    private String description;

    @Column(name = "price", nullable = false, precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2)")
    private BigDecimal price;

    @Column(name = "stock", nullable = false, columnDefinition = "SMALLINT")
    private Integer stock;

    @Column(name = "image_url", columnDefinition = "VARCHAR(255)")
    private String imageUrl;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "seller_id", nullable = false, updatable = false)
    @JsonBackReference
    private User seller;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Review> reviews = new HashSet<>();

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock must be greater than or equal to 0");
        }
        this.stock = stock;
    }

    public void addStock(Integer stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock must be greater than or equal to 0");
        }
        this.stock += stock;
    }

    public void removeStock(Integer stock) {
        if (stock > this.stock) {
            throw new IllegalArgumentException("Stock must be less than or equal to current stock");
        }
        this.stock -= stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be greater than or equal to 0");
        }
        this.price = price;
    }

    public Long getSellerId() {
        return seller.getId();
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public User getSeller() {
        return seller;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public void addReview(Review review) {
        reviews.add(review);
        review.setProduct(this);
    }

    public void removeReview(Review review) {
        reviews.remove(review);
        review.setProduct(null);
    }

    public void clearReviews() {
        reviews.clear();
    }

    public boolean isAvailable() {
        if (stock > 0) {
            return true;
        }
        return false;
    }

    public boolean isAvailable(Integer quantity) {
        if (stock >= quantity) {
            return true;
        }
        return false;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public void addCategory(Category category) {
        categories.add(category);
        category.getProducts().add(this);
    }

    public void removeCategory(Category category) {
        categories.remove(category);
        category.getProducts().remove(this);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Product{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append("[description hidden for brevity]").append('\''); // Optionally hide lengthy descriptions
        sb.append(", price=").append(price);
        sb.append(", stock=").append(stock);
        sb.append(", imageUrl='").append(imageUrl).append('\'');
        sb.append(", sellerId=").append(seller != null ? seller.getId() : "null");
        sb.append('}');
        return sb.toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equal(getName(), product.getName()) && Objects.equal(getSeller().getId(), product.getSeller().getId());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getName());
        result = 31 * result + Objects.hashCode(getSeller().getId());
        return result;
    }

    public static class Builder {
        private final Product product;

        public Builder() {
            product = new Product();
        }

        public Builder withName(String name) {
            product.name = name;
            return this;
        }

        public Builder withDescription(String description) {
            product.description = description;
            return this;
        }

        public Builder withPrice(BigDecimal price) {
            product.price = price;
            return this;
        }

        public Builder withImageUrl(String imageUrl) {
            product.imageUrl = imageUrl;
            return this;
        }

        public Builder withSeller(User seller) {
            product.seller = seller;
            return this;
        }

        public Builder withStock(Integer stock) {
            product.setStock(stock);
            return this;
        }

        public Builder withReviews(Set<Review> reviews) {
            product.reviews = reviews;
            return this;
        }

        public Product build() {
            return product;
        }
    }
}

