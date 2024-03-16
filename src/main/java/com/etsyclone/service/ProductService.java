package com.etsyclone.service;

import com.etsyclone.entity.Product;
import com.etsyclone.entity.User;
import com.etsyclone.repository.ProductRepository;
import com.etsyclone.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private Product newProduct;

    @Autowired
    public ProductService(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Product addProduct(Product product, Long userId) {
        User seller = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with id " + userId + " not found"));
        product.setSeller(seller);
        newProduct = productRepository.save(product);
        return newProduct;
    }

    @Transactional
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Product getProduct(Long id) {
        return productRepository.findById(id).get();
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product getProductByName(String name) {
        return productRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public Integer getProductsCount() {
        return productRepository.findAll().size();
    }

    @Transactional(readOnly = true)
    public Set<Product> getProductsByPrice(Double price) {
        return productRepository.findByPrice(price);
    }

    @Transactional(readOnly = true)
    public Set<Product> getProductsByPriceBetween(BigDecimal price1, BigDecimal price2) {
        return productRepository.findByPriceBetween(price1, price2);
    }

    @Transactional(readOnly = true)
    public Set<Product> getProductsByPriceLessThanEqual(Double price) {
        return productRepository.findByPriceLessThanEqual(price);
    }

    @Transactional(readOnly = true)
    public Set<Product> getProductsByPriceGreaterThanEqual(Double price) {
        return productRepository.findByPriceGreaterThanEqual(price);
    }

    @Transactional
    public Product updateProduct(Long id, Product productDetails) {
        Product productToUpdate = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));

        productToUpdate.setDescription(productDetails.getDescription());
        productToUpdate.setPrice(productDetails.getPrice());
        productToUpdate.setImageUrl(productDetails.getImageUrl());
        productToUpdate.setStock(productDetails.getStock());

        return productRepository.save(productToUpdate);
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
