package com.etsyclone.controller.rest;

import com.etsyclone.entity.Address;
import com.etsyclone.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressRestController {

    private final AddressService addressService;

    @Autowired
    public AddressRestController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public Address addAddress(@RequestBody Address address) {
        return addressService.saveAddress(address);
    }
    @GetMapping
    public List<Address> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    @GetMapping("/{id}")
    public Address getAddressById(@PathVariable Long id) {
        return addressService.getAddress(id);
    }

    @GetMapping("/{id}/user")
    public Address getAddressByUserId(@PathVariable Long userId) {
        return addressService.getAddressByCustomerId(userId);
    }

    @PutMapping("/{id}")
    public void updateAddress(@RequestBody Address address) {
        addressService.updateAddress(address);
    }

    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
    }
}
