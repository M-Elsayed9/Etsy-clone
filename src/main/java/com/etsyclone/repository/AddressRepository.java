package com.etsyclone.repository;

import com.etsyclone.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Set<Address> findByCustomer_Id(Long customerId);
}
