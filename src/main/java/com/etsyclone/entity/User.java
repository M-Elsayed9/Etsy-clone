package com.etsyclone.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.etsyclone.config.RoleName;
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
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "user", indexes = {
        @Index(name = "idx_user_name", columnList = "user_name"),
        @Index(name = "idx_email", columnList = "email")
})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false, unique = true, columnDefinition = "VARCHAR(50)", updatable = false)
    private String userName;

    @Column(name = "email", nullable = false, unique = true, columnDefinition = "VARCHAR(50)", updatable = false)
    private String email;

    @Column(name = "password", columnDefinition = "VARCHAR(100)", nullable = false)
    private String password;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false)
    )
    private Set<Role> roles;

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

    public User() {
    }

    public User(String userName, String email, String passwordHash) {
        this.userName = userName;
        this.email = email;
        this.password = passwordHash;
        this.roles = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String passwordHash) {
        this.password = passwordHash;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public void addAddress(Address address) {
        addresses.add(address);
        address.setCustomer(this);
    }

    public void removeAddress(Address address) {
        addresses.remove(address);
        address.setCustomer(null);
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public List<Product> getSellerProducts() {
        return sellerProducts;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void setOrders(HashSet<Order> orders) {
        this.orders = orders;
    }

    public void setSellerProducts(List<Product> products) {
        this.sellerProducts = products;
    }

    @JsonIgnore
    public boolean isGuest() {
        return roles.contains(RoleName.GUEST);
    }

    public boolean isCustomer() {
        return roles.contains(RoleName.CUSTOMER);
    }

    public boolean isSeller() {
        return roles.contains(RoleName.SELLER);
    }

    public boolean isAdmin() {
        return roles.contains(RoleName.ADMIN);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> (GrantedAuthority) () -> "ROLE_" + role.getRole())
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void addOrder(Order order) {
        orders.add(order);
        order.setCustomer(this);
    }

    public void removeOrder(Order order) {
        orders.remove(order);
        order.setCustomer(null);
    }

    public void addProduct(Product product) {
        sellerProducts.add(product);
        product.setSeller(this);
    }

    public void removeProduct(Product product) {
        sellerProducts.remove(product);
        product.setSeller(null);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(id);
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", roles=").append(roles.toString());
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equal(getUserName(), user.getUserName()) && Objects.equal(getEmail(), user.getEmail());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getUserName());
        result = 31 * result + Objects.hashCode(getEmail());
        return result;
    }

    public void addRole(Role role) {
        roles.add(role);
    }
}
