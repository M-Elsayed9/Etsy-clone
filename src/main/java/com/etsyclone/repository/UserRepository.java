package com.etsyclone.repository;

import com.etsyclone.entity.RoleName;
import com.etsyclone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
        User findByEmail(String email);

        User findByUserName(String userName);
}