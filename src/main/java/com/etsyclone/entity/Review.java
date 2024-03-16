package com.etsyclone.entity;

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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "review", indexes = {
        @Index(name = "idx_product_id", columnList = "product_id"),
        @Index(name = "idx_rating", columnList = "rating")
})
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, updatable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id")
    private User customer;

    @Lob
    @Column(name = "comment", nullable = false, columnDefinition = "TEXT", length = 1000, updatable = false)
    private String comment;

    @Column(name = "rating", columnDefinition = "SMALLINT")
    private Short rating;

    public Review() {
    }

    public Review(Product product, User customer, String comment, Short rating) {
        this.product = product;
        if(customer != null) {
            this.customer = customer;
        }
        this.comment = comment;
        if (rating != null) {
            this.rating = rating;
        }
    }

    public Review(Product product, String comment, Short rating) {
        this.product = product;
        this.comment = comment;
        this.rating = rating;
    }

    public Review(Product product, String comment) {
        this.product = product;
        this.comment = comment;
    }

    public Review(Product product, User customer, String comment) {
        this.product = product;
        this.customer = customer;
        this.comment = comment;
        this.rating = 0;
    }

    public Review builder() {
        return new Review(product, customer, comment, rating);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public User getCustomer() {
        return customer;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Short getRating() {
        return rating;
    }

    public void setRating(Short rating) {
        this.rating = rating;
    }

    public Long getProductId() {
        return product.getId();
    }

    public Long getCustomerId() {
        return customer.getId();
    }

    public String getCustomerUserName() {
        return customer.getUserName();
    }

    public String getProductName() {
        return product.getName();
    }

    public String getProductSellerName() {
        return product.getSeller().getUserName();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Review{");
        sb.append("id=").append(id);
        sb.append(", productId=").append(product != null ? product.getId() : "null");
        sb.append(", customerId=").append(customer != null ? customer.getId() : "null");
        sb.append(", comment='").append(comment).append('\'');
        sb.append(", rating=").append(rating);
        sb.append('}');
        return sb.toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equal(getProduct().getId(), review.getProduct().getId())
                && Objects.equal(getCustomer().getId(), review.getCustomer().getId())
                && Objects.equal(getComment(), review.getComment());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getProduct().getId());
        result = 31 * result + Objects.hashCode(getCustomer().getId());
        result = 31 * result + Objects.hashCode(getComment());
        return result;
    }
}

