package com.etsyclone.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 50, unique = true, updatable = false)
    private RoleName role;

    public Role() {
    }

    public Role(RoleName role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleName getRole() {
        return role;
    }

    public String getRoleName() {
        return role.name();
    }

    public void setRole(RoleName role) {
        this.role = role;
    }

    public boolean isCustomer() {
        return role.equals(RoleName.CUSTOMER);
    }

    public boolean isSeller() {
        return role.equals(RoleName.SELLER);
    }

    public boolean isAdmin() {
        return role.equals(RoleName.ADMIN);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(this.role, role.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(role);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Role{");
        sb.append("id=").append(id);
        sb.append(", role=").append(role);
        sb.append('}');
        return sb.toString();
    }
}
