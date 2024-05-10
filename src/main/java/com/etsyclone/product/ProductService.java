package com.etsyclone.product;

import com.etsyclone.product.ProductDTO;
import com.etsyclone.product.Product;
import com.etsyclone.user.User;
import com.etsyclone.product.ProductRepository;
import com.etsyclone.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ProductDTO addProduct(ProductDTO productDTO, Long userId) {
        User seller = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with id " + userId + " not found"));
        Product product = convertToEntity(productDTO);
        product.setSeller(seller);
        Product savedProduct = productRepository.save(product);
        seller.addProduct(savedProduct);
        return convertToDTO(savedProduct);
    }

    @Transactional(readOnly = true)
    public ProductDTO getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));
        return convertToDTO(product);
    }

    @Transactional(readOnly = true)
    public ProductDTO getProduct(String name) {
        Product product = productRepository.findByName(name);
        if (product == null) {
            throw new RuntimeException("Product not found with name " + name);
        }
        return convertToDTO(product);
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product productToUpdate = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));

        if (productDTO.getDescription() != null) {
            productToUpdate.setDescription(productDTO.getDescription());
        }
        if (productDTO.getPrice() != null) {
            productToUpdate.setPrice(productDTO.getPrice());
        }
        if (productDTO.getImageUrl() != null) {
            productToUpdate.setImageUrl(productDTO.getImageUrl());
        }
        if (productDTO.getStock() != null) {
            int newStock = productToUpdate.getStock() + productDTO.getStock();
            if (newStock < 0) {
                throw new IllegalArgumentException("Resulting stock cannot be negative.");
            }
            productToUpdate.setStock(newStock);
        }

        Product updatedProduct = productRepository.save(productToUpdate);
        return convertToDTO(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));
        productRepository.delete(product);
        product.getSeller().removeProduct(product);
    }

    private ProductDTO convertToDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getImageUrl()
        );
    }

    private Product convertToEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setImageUrl(productDTO.getImageUrl());
        return product;
    }
}
