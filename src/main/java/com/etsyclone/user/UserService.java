package com.etsyclone.user;

import com.etsyclone.cart.Cart;
import com.etsyclone.order.Order;
import com.etsyclone.role.Role;
import com.etsyclone.role.RoleName;
import com.etsyclone.cart.CartService;
import com.etsyclone.role.RoleService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
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
        user.addRole(role);
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
        return userRepository.findByUsername(userName);
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