package com.etsyclone.repository;

import com.etsyclone.entity.Category;
import com.etsyclone.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);

    Set<Product> findByPriceBetween(BigDecimal price1, BigDecimal price2);

    Set<Product> findByPrice(Double price);

    Set<Product> findByPriceLessThanEqual(Double price);

    Set<Product> findByPriceGreaterThanEqual(Double price);
}
