package com.etsyclone.config;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.etsyclone.entity.Role;
import com.etsyclone.entity.RoleName;
import com.etsyclone.repository.RoleRepository;

@Configuration
public class RolesLoader {

    @Bean
    CommandLineRunner initDatabase(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.count() == 0) {
                roleRepository.save(new Role(RoleName.ADMIN));
                roleRepository.save(new Role(RoleName.CUSTOMER));
                roleRepository.save(new Role(RoleName.SELLER));
            }
        };
    }
}
