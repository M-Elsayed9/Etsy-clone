package com.etsyclone.entity;

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

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<CartItem> items = new HashSet<>();

    public Cart() {
    }

    public Cart(User customer) {
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public User getCustomer() {
        return customer;
    }

    public Set<CartItem> getItems() {
        return items;
    }

    public void addItem(CartItem item) {
        items.add(item);
    }

    public void clearItems() {
        items.clear();
    }

    public void removeItem(CartItem item) {
        items.remove(item);
    }

    public BigDecimal getTotalPrice() {
        return items.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void setItems(Set<CartItem> items) {
        this.items = items;
    }

    public void setTotalPrice(Number totalPrice) {
        this.totalPrice = (BigDecimal) totalPrice;
    }

    public void updateItem(CartItem item) {
        items.stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(item.getProduct().getId()))
                .findFirst()
                .ifPresent(cartItem -> cartItem.setQuantity(item.getQuantity()));
    }

    public void clearCart() {
        items.clear();
    }

    public void removeItem(Product product) {
        items.removeIf(cartItem -> cartItem.getProduct().getId().equals(product.getId()));
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Cart{");
        sb.append("id=").append(id);
        sb.append(", totalPrice=").append(totalPrice);
        sb.append(", customer=").append(customer);
        sb.append(", items=").append(items);
        for (CartItem item : items) {
            sb.append(item.getProduct().getName());
        }
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(getCustomer(), cart.getCustomer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer);
    }
}
