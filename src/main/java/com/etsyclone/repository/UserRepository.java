package com.etsyclone.repository;

import com.etsyclone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {


        User findByEmail(String email);

        User findByUserName(String userName);
}