package com.favorites.service;

import com.favorites.domain.Category;
import com.favorites.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category addCategory(String name, Long userId) {
        Category existCategory = categoryRepository.findByUserIdAndName(userId, name);
        if (existCategory != null) {
            throw new RuntimeException("Category name already exists!");
        }
        Category category = new Category();
        category.setName(name);
        category.setUserId(userId);
        category.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return categoryRepository.save(category);
    }

    public List<Category> getUserCategories(Long userId) {
        return categoryRepository.findByUserIdOrderBySortOrderAsc(userId);
    }

    public Category updateCategory(Long categoryId, String newName, Long userId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null || !category.getUserId().equals(userId)) {
            throw new RuntimeException("Category not found or no permission!");
        }
        Category existCategory = categoryRepository.findByUserIdAndName(userId, newName);
        if (existCategory != null && !existCategory.getId().equals(categoryId)) {
            throw new RuntimeException("Category name already exists!");
        }
        category.setName(newName);
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long categoryId, Long userId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null || !category.getUserId().equals(userId)) {
            throw new RuntimeException("Category not found or no permission!");
        }
        categoryRepository.delete(category);
    }

    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }
}