package com.etsyclone.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.google.common.base.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "address", indexes = {
        @Index(name = "idx_customer_id", columnList = "customer_id")
})
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

    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "customer_id", nullable = false, updatable = false)
    @JsonBackReference
    private User customer;

    public Address() {
    }

    public Address(String street, String city, String state, String zipCode, User customer) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
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
