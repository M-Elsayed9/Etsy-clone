package com.etsyclone.dto;

public class ReviewDTO {

    private Long productId;
    private Long customerId;
    private String comment;
    private Short rating;

    public ReviewDTO() {}

    public ReviewDTO(Long productId, Long customerId, String comment, Short rating) {
        this.productId = productId;
        this.customerId = customerId;
        this.comment = comment;
        this.rating = rating;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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
}
