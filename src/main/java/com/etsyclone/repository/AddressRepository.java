package com.etsyclone.repository;

import com.etsyclone.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Address findByCustomer_Id(Long customerId);
}
