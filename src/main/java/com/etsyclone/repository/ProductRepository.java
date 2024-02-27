package com.etsyclone.repository;

import com.etsyclone.entity.Category;
import com.etsyclone.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);

    Set<Product> findByPriceBetween(BigDecimal price1, BigDecimal price2);

    Set<Product> findByPrice(Double price);

    Set<Product> findByPriceLessThanEqual(Double price);

}
