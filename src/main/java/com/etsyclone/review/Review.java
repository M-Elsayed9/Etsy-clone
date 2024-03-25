package com.etsyclone.review;

import com.etsyclone.product.Product;
import com.etsyclone.user.User;
import com.google.common.base.Objects;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "review", indexes = {
        @Index(name = "idx_rating", columnList = "rating")
})
@Getter
@Setter
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
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

    public Long getProductId() {
        return product.getId();
    }

    public Long getCustomerId() {
        return customer.getId();
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

