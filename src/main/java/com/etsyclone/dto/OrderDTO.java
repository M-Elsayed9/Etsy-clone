package com.etsyclone.dto;

import java.util.List;

public class OrderDTO {

    private Long userId;
    private AddressDTO shippingAddress;
    private List<OrderItemDTO> orderItems;

    public OrderDTO() {}

    public OrderDTO(Long userId, AddressDTO shippingAddress, List<OrderItemDTO> orderItems) {
        this.userId = userId;
        this.shippingAddress = shippingAddress;
        this.orderItems = orderItems;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public AddressDTO getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(AddressDTO shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }

    public static class OrderItemDTO {

        private Long productId;
        private Short quantity;

        public OrderItemDTO() {}

        public OrderItemDTO(Long productId, Short quantity) {
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
}
