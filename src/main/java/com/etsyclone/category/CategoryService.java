package com.etsyclone.category;

import com.etsyclone.category.Category;
import com.etsyclone.product.Product;
import com.etsyclone.category.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Set;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    @Transactional
    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public Integer getCategoryCount() {
        return categoryRepository.findAll().size();
    }

    @Transactional(readOnly = true)
    public Set<Category> getAllCategories() {
        return Set.copyOf(categoryRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Category getCategory(Long id) {
        return categoryRepository.findById(id).get();
    }

    @Transactional(readOnly = true)
    public Set<Product> getCategoryProducts(Long id) {
        return categoryRepository.findById(id).get().getProducts();
    }

    @Transactional
    public void updateCategory(Category category) {
        categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

}
