package com.etsyclone.cart;

import com.etsyclone.cartItem.CartItem;
import com.etsyclone.product.Product;
import com.etsyclone.user.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.base.Objects;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cart")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Getter
@Setter
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_price", precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2)", nullable = false)
    private BigDecimal totalPrice;

    @OneToOne(optional = false)
    @JoinColumn(name = "customer_id", unique = true, nullable = false, updatable = false)
    @JsonIgnore
    private User customer;

    @OneToMany(mappedBy = "cart", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<CartItem> items;

    public Cart(User customer) {
        this.customer = customer;
        this.totalPrice = BigDecimal.ZERO;
        this.items = new HashSet<>();
    }

    public void setItems(Set<CartItem> items) {
        this.items = items;
        totalPrice = getTotalPrice();
    }

    public void addItem(CartItem item) {
        item.setCart(this);
        items.add(item);
        totalPrice = getTotalPrice();
    }

    public void removeItem(CartItem item) {
        items.remove(item);
        totalPrice = getTotalPrice();
    }

    public BigDecimal getTotalPrice() {
        return items.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        if (totalPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total price cannot be negative");
        }
        this.totalPrice = getTotalPrice();
    }

    public void updateItem(CartItem item) {
        items.stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(item.getProduct().getId()))
                .findFirst()
                .ifPresent(cartItem -> cartItem.setQuantity(item.getQuantity()));
        totalPrice = getTotalPrice();
    }

    public void clearCart() {
        items.clear();
        totalPrice = BigDecimal.ZERO;
    }

    public void removeItem(Product product) {
        items.removeIf(cartItem -> cartItem.getProduct().getId().equals(product.getId()));
        totalPrice = getTotalPrice();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Cart{");
        sb.append("id=").append(id);
        sb.append(", totalPrice=").append(totalPrice);
        sb.append(", customerId=").append(customer.getId());
        sb.append(", itemsCount=").append(items.size());
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equal(getCustomer().getId(), cart.getCustomer().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getCustomer().getId());
    }
}
