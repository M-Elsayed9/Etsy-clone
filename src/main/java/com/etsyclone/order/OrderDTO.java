package com.etsyclone.order;

import com.etsyclone.address.AddressDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class OrderDTO {

    private String username;
    private AddressDTO shippingAddress;
    private BigDecimal totalPrice;
    private Set<OrderItemDTO> orderItems;

    public void setTotalPrice(BigDecimal totalPrice) {
        if (totalPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total price cannot be negative");
        }
        this.totalPrice = totalPrice;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class OrderItemDTO {

        private Long productId;
        private Short quantity;

        public OrderItemDTO(Long productId, Short quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public void setQuantity(Short quantity) {
            if (quantity < 0) {
                throw new IllegalArgumentException("Quantity cannot be negative");
            }
            this.quantity = quantity;
        }
    }
}
