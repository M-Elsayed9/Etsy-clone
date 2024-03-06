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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Lob
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
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
        this.stock = stock;
    }

    public void addStock(Integer stock) {
        this.stock += stock;
    }

    public void removeStock(Integer stock) {
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", sellerId=" + seller.getId() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equal(name, product.name) && Objects.equal(seller, product.seller);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(seller);
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

        public Builder withCategory(Category category) {
            product.category = category;
            return this;
        }

        public Builder withSeller(User seller) {
            product.seller = seller;
            return this;
        }

        public Builder withStock(Integer stock) {
            product.stock = stock;
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

