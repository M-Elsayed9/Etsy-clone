package com.etsyclone.user;

import com.etsyclone.address.Address;
import com.etsyclone.cart.Cart;
import com.etsyclone.order.Order;
import com.etsyclone.product.Product;
import com.etsyclone.role.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.base.Objects;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user", indexes = {
        @Index(name = "idx_user_name", columnList = "user_name"),
        @Index(name = "idx_email", columnList = "email")
})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false, unique = true, columnDefinition = "VARCHAR(50)", updatable = false)
    private String username;

    @Column(name = "email", nullable = false, unique = true, columnDefinition = "VARCHAR(50)", updatable = false)
    private String email;

    @Column(name = "password", columnDefinition = "VARCHAR(100)", nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private Set<Address> addresses;

    @JsonIgnore
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private Set<Order> orders;

    @JsonIgnore
    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private List<Product> sellerProducts;

    @JsonIgnore
    @OneToOne(mappedBy = "customer", orphanRemoval = true, fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private Cart cart;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void addAddress(Address address) {
        addresses.add(address);
        address.setCustomer(this);
    }

    public void addOrder(Order order) {
        orders.add(order);
        order.setCustomer(this);
    }

    public void addProduct(Product product) {
        sellerProducts.add(product);
        product.setSeller(this);
    }

    public void removeProduct(Product product) {
        sellerProducts.remove(product);
        product.setSeller(null);
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(id);
        sb.append(", userName='").append(username).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", roles=").append(roles);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equal(getUsername(), user.getUsername()) && Objects.equal(getEmail(), user.getEmail());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getUsername());
        result = 31 * result + Objects.hashCode(getEmail());
        return result;
    }
}
