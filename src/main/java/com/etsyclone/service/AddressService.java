package com.etsyclone.service;

import com.etsyclone.entity.Address;
import com.etsyclone.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Transactional
    public Address addAddress(Address address) {
        return addressRepository.save(address);
    }

    @Transactional
    public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }

    @Transactional(readOnly = true)
    public Address getAddress(Long id) {
        return addressRepository.findById(id).get();
    }

    @Transactional(readOnly = true)
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Address getAddressByCustomerId(Long userId) {
        return addressRepository.findByCustomer_Id(userId);
    }

    @Transactional
    public void updateAddress(Address address) {
        addressRepository.save(address);
    }

    @Transactional
    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }

}
