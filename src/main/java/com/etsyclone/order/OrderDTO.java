package com.etsyclone.order;

import com.etsyclone.address.AddressDTO;
import com.etsyclone.orderitem.OrderItemDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class OrderDTO {

    private Long userId;
    private AddressDTO shippingAddress;
    private Set<OrderItemDTO> orderItems;
    private BigDecimal total;

    public void setTotal(BigDecimal total) {
        if (total.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total cannot be negative");
        }
        this.total = total;
    }
}
