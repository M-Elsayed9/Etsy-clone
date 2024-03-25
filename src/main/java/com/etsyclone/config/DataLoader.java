package com.etsyclone.config;
import com.etsyclone.category.Category;
import com.etsyclone.role.RoleName;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.etsyclone.role.Role;
import com.etsyclone.role.RoleRepository;
import com.etsyclone.category.CategoryRepository;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(RoleRepository roleRepository, CategoryRepository categoryRepository) {
        return args -> {
            if (roleRepository.count() == 0) {
                // ROLES = { 'ADMIN', 'CUSTOMER', 'SELLER', 'GUEST }
                roleRepository.save(new Role(RoleName.GUEST));
                roleRepository.save(new Role(RoleName.ADMIN));
                roleRepository.save(new Role(RoleName.CUSTOMER));
                roleRepository.save(new Role(RoleName.SELLER));
            }

            if(categoryRepository.count() == 0) {
                // CATEGORIES = { 'Handmade Goods', 'Vintage Items',
                // 'Craft Supplies', 'Art & Collectibles', 'Jewelry',
                // 'Clothing', 'Home & Living', 'Wedding & Party',
                // 'Toys & Entertainment', 'Others' }
                categoryRepository.save(new Category("Handmade Goods"));
                categoryRepository.save(new Category("Vintage Items"));
                categoryRepository.save(new Category("Craft Supplies"));
                categoryRepository.save(new Category("Art & Collectibles"));
                categoryRepository.save(new Category("Jewelry"));
                categoryRepository.save(new Category("Clothing"));
                categoryRepository.save(new Category("Home & Living"));
                categoryRepository.save(new Category("Wedding & Party"));
                categoryRepository.save(new Category("Toys & Entertainment"));
                categoryRepository.save(new Category("Others"));
            }
        };
    }
}
