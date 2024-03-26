package com.etsyclone.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {

    private String username;
    private String email;
    private String password;

    public UserDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User toUser() {
        return new User(username, email, password);
    }

    public static UserDTO fromUser(User user) {
        return new UserDTO(user.getUsername(), user.getEmail(), user.getPassword());
    }
}
