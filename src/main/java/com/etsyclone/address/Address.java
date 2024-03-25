package com.etsyclone.address;

import com.etsyclone.user.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.base.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "address")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Getter
@Setter
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "street", nullable = false, columnDefinition = "VARCHAR(100)", updatable = false)
    private String street;

    @Column(name = "city", nullable = false, columnDefinition = "VARCHAR(50)", updatable = false)
    private String city;

    @Column(name = "state", nullable = false, columnDefinition = "VARCHAR(50)", updatable = false)
    private String state;

    @Column(name = "zip_code", nullable = false, columnDefinition = "VARCHAR(10)", updatable = false)
    private String zipCode;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false, updatable = false)
    private User customer;

    public Address(String street, String city, String state, String zipCode, User customer) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.customer = customer;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Address{");
        sb.append("id=").append(id);
        sb.append(", street='").append(street).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", state='").append(state).append('\'');
        sb.append(", zipCode='").append(zipCode).append('\'');
        sb.append(", customerId=").append(customer != null ? customer.getId() : "null");
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equal(getStreet(),
                address.getStreet()) && Objects.equal(getCity(),
                address.getCity()) && Objects.equal(getState(),
                address.getState()) && Objects.equal(getZipCode(),
                address.getZipCode()) && Objects.equal(getCustomer().getId(),
                address.getCustomer().getId());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getStreet());
        result = 31 * result + Objects.hashCode(getCity());
        result = 31 * result + Objects.hashCode(getState());
        result = 31 * result + Objects.hashCode(getZipCode());
        result = 31 * result + Objects.hashCode(getCustomer().getId());
        return result;
    }
}
