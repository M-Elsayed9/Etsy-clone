package com.etsyclone.cartItem;

import com.etsyclone.cart.Cart;
import com.etsyclone.product.Product;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.base.Objects;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_item")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Getter
@Setter
@NoArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "cart_id", nullable = false, updatable = false)
    private Cart cart;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false, updatable = false)
    private Product product;

    @Column(name = "quantity", nullable = false, columnDefinition = "SMALLINT UNSIGNED")
    private Short quantity;

    public CartItem(Cart cart, Product product, Short quantity) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public void setQuantity(Short quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
    }

    public Long getCartId() {
        return cart.getId();
    }

    public Long getProductId() {
        return product.getId();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CartItem{");
        sb.append("id=").append(id);
        sb.append(", cartId=").append(cart != null ? cart.getId() : "null");
        sb.append(", productId=").append(product != null ? product.getId() : "null");
        sb.append(", quantity=").append(quantity);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equal(getCart().getId(), cartItem.getCart().getId())
                && Objects.equal(getProduct().getId(), cartItem.getProduct().getId());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getCart().getId());
        result = 31 * result + Objects.hashCode(getProduct().getId());
        return result;
    }
}
