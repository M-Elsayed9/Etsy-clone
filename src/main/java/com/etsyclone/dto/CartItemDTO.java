package com.etsyclone.dto;

public class CartItemDTO {

    private Long productId;
    private Short quantity;

    public CartItemDTO() {}

    public CartItemDTO(Long productId, Short quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Short getQuantity() {
        return quantity;
    }

    public void setQuantity(Short quantity) {
        this.quantity = quantity;
    }
}
