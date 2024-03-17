package com.etsyclone.service;

import com.etsyclone.entity.Cart;
import com.etsyclone.entity.Order;
import com.etsyclone.entity.Role;
import com.etsyclone.config.RoleName;
import com.etsyclone.entity.User;
import com.etsyclone.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final CartService cartService;

    @Autowired
    public UserService(UserRepository userRepository, RoleService roleService, CartService cartService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.cartService = cartService;
    }

    @Transactional
    public User addUser(User user, RoleName roleName) {
        Role role = roleService.getRoleByName(roleName)
                .orElseGet(() -> {
                    Role newRole = new Role(roleName);
                    roleService.addRole(newRole);
                    return newRole;
                });

        user.setRoles(new HashSet<>(Collections.singletonList(role)));
        return userRepository.save(user);
    }

    @Transactional
    public User addCustomer(User user) {
        User customer = addUser(user, RoleName.CUSTOMER);
        Cart cart = new Cart(customer);
        cartService.addCart(cart);
        customer.setCart(cart);
        return userRepository.save(customer);
    }

    @Transactional
    public User addSeller(User user) {
        User seller = addUser(user, RoleName.SELLER);
        return userRepository.save(seller);
    }

    @Transactional
    public User addAdmin(User user) {
        return addUser(user, RoleName.ADMIN);
    }

    @Transactional(readOnly = true)
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
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
    public Set<Order> getUserOrders(Long id) {
        User user = getUser(id);
        return user.getOrders();    }

    @Transactional(readOnly = true)
    public Order getUserOrder(Long id, Long orderId) {
        return userRepository.findById(id).get().getOrders().stream().filter(order -> order.getId().equals(orderId)).findFirst().get();
    }

    @Transactional
    public User updateUser(Long id, User userDetails) {
        User existingUser = getUser(id);
        existingUser.setPassword(userDetails.getPassword());
        return userRepository.save(existingUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
