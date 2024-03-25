package com.etsyclone.orderitem;

import com.etsyclone.product.Product;
import com.etsyclone.order.Order;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.base.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "order_item")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Getter
@Setter
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false, updatable = false)
    private Order order;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, updatable = false)
    private Product product;

    @Column(name = "quantity", nullable = false, columnDefinition = "SMALLINT UNSIGNED", updatable = false)
    private Short quantity;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)", updatable = false)
    private BigDecimal price;

    public OrderItem(Order order, Product product, Short quantity, BigDecimal price) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public BigDecimal getTotalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public Long getProductId() {
        return product.getId();
    }

    public Long getOrderId() {
        return order.getId();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrderItem{");
        sb.append("id=").append(id);
        sb.append(", orderId=").append(order != null ? order.getId() : "null");
        sb.append(", productId=").append(product != null ? product.getId() : "null");
        sb.append(", productName='").append(product != null ? product.getName() : "null").append('\'');
        sb.append(", quantity=").append(quantity);
        sb.append(", price=").append(price);
        sb.append('}');
        return sb.toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equal(getOrder().getId(), orderItem.getOrder().getId())
                && Objects.equal(getProduct().getId(), orderItem.getProduct().getId());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getOrder().getId());
        result = 31 * result + Objects.hashCode(getProduct().getId());
        return result;
    }
}
