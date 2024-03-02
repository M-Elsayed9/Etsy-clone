package com.etsyclone.service;

import com.etsyclone.entity.Order;
import com.etsyclone.entity.Role;
import com.etsyclone.entity.RoleName;
import com.etsyclone.entity.User;
import com.etsyclone.repository.RoleRepository;
import com.etsyclone.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public User addCustomer(User user) {
        Optional<Role> customerRoleOpt = roleRepository.findByRole(RoleName.CUSTOMER);

        Role customerRole;
        if (customerRoleOpt.isPresent()) {
            customerRole = customerRoleOpt.get();
        } else {
            customerRole = new Role(RoleName.CUSTOMER);
            roleRepository.save(customerRole);
        }

        user.setRoles(new HashSet<>(Collections.singletonList(customerRole)));
        return userRepository.save(user);
    }

    @Transactional
    public User addSeller(User user) {
        Optional<Role> sellerRoleOpt = roleRepository.findByRole(RoleName.SELLER);

        Role sellerRole;
        if (sellerRoleOpt.isPresent()) {
            sellerRole = sellerRoleOpt.get();
        } else {
            sellerRole = new Role(RoleName.SELLER);
            roleRepository.save(sellerRole);
        }

        user.setRoles(new HashSet<>(Collections.singletonList(sellerRole)));
        return userRepository.save(user);
    }

    @Transactional
    public User addAdmin(User user) {
        Optional<Role> adminRoleOpt = roleRepository.findByRole(RoleName.ADMIN);

        Role adminRole;
        if (adminRoleOpt.isPresent()) {
            adminRole = adminRoleOpt.get();
        } else {
            adminRole = new Role(RoleName.ADMIN);
            roleRepository.save(adminRole);
        }

        user.setRoles(new HashSet<>(Collections.singletonList(adminRole)));
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getUser(Long id) {
        return userRepository.findById(id).get();
    }

    @Transactional(readOnly = true)
    public Set<User> getAllUsers() {
        return Set.copyOf(userRepository.findAll());
    }

    @Transactional(readOnly = true)

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public User getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Transactional(readOnly = true)
    public Set<Role> getUserRoles(User user) {
        return user.getRoles();
    }

    @Transactional(readOnly = true)
    public Set<Order> getUserOrders(Long id) {
        User user = getUser(id); //
        return user.getOrders();    }

    @Transactional(readOnly = true)
    public Order getUserOrder(Long id, Long orderId) {
        return userRepository.findById(id).get().getOrders().stream().filter(order -> order.getId().equals(orderId)).findFirst().get();
    }

    @Transactional
    public User updateUser(User user) {
        User updatedUser = userRepository.save(user);
        return updatedUser;
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
