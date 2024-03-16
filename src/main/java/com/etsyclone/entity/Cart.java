package com.etsyclone.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.common.base.Objects;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cart", indexes = {
        @Index(name = "idx_customer_id", columnList = "customer_id"),
})
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_price", precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2)")
    private BigDecimal totalPrice;

    @OneToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "customer_id", unique = true)
    @JsonBackReference
    private User customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<CartItem> items;

    public Cart() {
    }

    public Cart(User customer) {
        this.customer = customer;
        this.totalPrice = BigDecimal.ZERO;
        this.items = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public Set<CartItem> getItems() {
        return items;
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
        sb.append(", customerId=").append(customer != null ? customer.getId() : "null");
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
