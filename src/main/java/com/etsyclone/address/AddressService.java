package com.etsyclone.address;

import com.etsyclone.address.AddressDTO;
import com.etsyclone.address.Address;
import com.etsyclone.user.User;
import com.etsyclone.address.AddressRepository;
import com.etsyclone.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AddressDTO addAddress(AddressDTO addressDTO, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Address address = convertToEntity(addressDTO);
        address.setCustomer(user);
        address = addressRepository.save(address);
        user.addAddress(address);
        return convertToDTO(address);
    }

    @Transactional(readOnly = true)
    public AddressDTO getAddressDTO(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new RuntimeException("Address not found with id: " + id));
        return convertToDTO(address);
    }

    @Transactional(readOnly = true)
    public Set<AddressDTO> getAllAddressesByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return user.getAddresses().stream().map(this::convertToDTO).collect(Collectors.toSet());
    }

    @Transactional
    public AddressDTO updateAddress(Long id, AddressDTO addressDTO) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new RuntimeException("Address not found with id: " + id));
        updateEntityWithDTO(address, addressDTO);
        address = addressRepository.save(address);
        return convertToDTO(address);
    }

    @Transactional
    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
        userRepository.findAll().forEach(user -> user.getAddresses().removeIf(address -> address.getId().equals(id)));
    }

    private AddressDTO convertToDTO(Address address) {
        return new AddressDTO(address.getStreet(), address.getCity(), address.getState(), address.getZipCode());
    }

    private Address convertToEntity(AddressDTO addressDTO) {
        Address address = new Address();
        address.setStreet(addressDTO.getStreet());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setZipCode(addressDTO.getZipCode());
        return address;
    }

    private void updateEntityWithDTO(Address address, AddressDTO addressDTO) {
        address.setStreet(addressDTO.getStreet());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setZipCode(addressDTO.getZipCode());
    }
}
