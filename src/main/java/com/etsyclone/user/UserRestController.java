package com.etsyclone.user;

import com.etsyclone.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {
        String token = userService.login(userDTO);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/auth/signout")
    public ResponseEntity<String> signout(@RequestBody UserDTO userDTO) {
        String token = userService.logout();
        return ResponseEntity.ok(token);
    }

    @PostMapping("/auth/customers")
    public ResponseEntity<UserDTO> customerRegistration(@RequestBody UserDTO userDTO) {
        UserDTO authenticatedUser = userService.customerRegistration(userDTO);
        return ResponseEntity.ok(authenticatedUser);
    }

    @PostMapping("/auth/sellers")
    public ResponseEntity<UserDTO> sellerRegistration(@RequestBody UserDTO userDTO) {
        UserDTO authenticatedUser = userService.sellerRegistration(userDTO);
        return ResponseEntity.ok(authenticatedUser);
    }

    @PostMapping("/auth/admins")
    public ResponseEntity<UserDTO> adminRegistration(@RequestBody UserDTO userDTO) {
        UserDTO authenticatedUser = userService.adminRegistration(userDTO);
        return ResponseEntity.ok(authenticatedUser);
    }
    @GetMapping
    public Set<User> getUsers() {
       return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @GetMapping("/{id}/orders")
    public Set<Order> getUserOrders(@PathVariable Long id) {
        return userService.getUserOrders(id);
    }

    @GetMapping("/{id}/orders/{orderId}")
    public Order getUserOrder(@PathVariable Long id, @PathVariable Long orderId) {
        return userService.getUserOrder(id, orderId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}