package com.etsyclone.controller.rest;

import com.etsyclone.entity.Order;
import com.etsyclone.entity.User;
import com.etsyclone.service.UserService;
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

    @PostMapping("/customers")
    public User addCustomer(@RequestBody User user) {
        User savedUser = userService.addCustomer(user);
        return savedUser;
    }

    @PostMapping("/sellers")
    public User addSeller(@RequestBody User user) {
        User savedUser = userService.addSeller(user);
        return savedUser;
    }

    @PostMapping("/admins")
    public User addAdmin(@RequestBody User user) {
        User savedUser = userService.addAdmin(user);
        return savedUser;
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