package com.etsyclone.entity;

import com.google.common.base.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Lob
    @Column(name = "comment", nullable = false, columnDefinition = "TEXT")
    private String comment;

    @Column(name = "rating")
    private Short rating;

    public Review() {
    }

    public Review(Product product, User user, String comment, Short rating) {
        this.product = product;
        this.user = user;
        this.comment = comment;
        this.rating = rating;
    }

    public Review(Product product, User user, String comment) {
        this.product = product;
        this.user = user;
        this.comment = comment;
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

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
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

    public Long getUserId() {
        return user.getId();
    }

    public String getUserName() {
        return user.getUserName();
    }

    public String getProductName() {
        return product.getName();
    }

    public String getProductImageUrl() {
        return product.getImageUrl();
    }

    public String getProductSellerName() {
        return product.getSeller().getUserName();
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", product=" + product +
                ", user=" + user +
                ", comment='" + comment + '\'' +
                ", rating=" + rating +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equal(product, review.product) && Objects.equal(user, review.user) && Objects.equal(comment, review.comment);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(product);
        result = 31 * result + Objects.hashCode(user);
        result = 31 * result + Objects.hashCode(comment);
        return result;
    }
}

