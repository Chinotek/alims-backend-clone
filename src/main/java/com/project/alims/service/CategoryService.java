package com.project.alims.service;

import com.project.alims.model.Category;
import com.project.alims.model.Laboratory;
import com.project.alims.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category createCategory(Category category) {
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        return categoryRepository.save(category);
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> findByLaboratory(Laboratory laboratory) {
        return categoryRepository.findByLaboratory(laboratory);
    }

    public Optional<Category> findByShortName(String shortName) {
        return Optional.ofNullable(categoryRepository.findByShortName(shortName));
    }

    public Optional<Category> findBySubcategory(String subcategory) {
        return Optional.ofNullable(categoryRepository.findBySubcategory(subcategory));
    }

    public Category updateCategory(Long id, Category updatedCategory) {
        Optional<Category> existingCategory = categoryRepository.findById(id);

        if (existingCategory.isPresent()) {
            Category category = existingCategory.get();
            category.setShortname(updatedCategory.getShortname());
            category.setSubcategory(updatedCategory.getSubcategory());
            category.setLaboratory(updatedCategory.getLaboratory());
            category.setUpdatedAt(LocalDateTime.now());

            return categoryRepository.save(category);
        } else {
            throw new RuntimeException("Category not found with ID: " + id);
        }
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
