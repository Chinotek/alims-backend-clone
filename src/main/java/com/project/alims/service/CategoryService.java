package com.project.alims.service;

import com.project.alims.model.Category;
import com.project.alims.model.Laboratory;
import com.project.alims.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findByShortName(String shortName) {
        return Optional.ofNullable(categoryRepository.findByShortName(shortName));
    }

    public List<Category> findBySubcategory(String subcategory) {
        List<Category> subcategory1Matches = categoryRepository.findBySubcategory1(subcategory);
        List<Category> subcategory2Matches = categoryRepository.findBySubcategory2(subcategory);

        List<Category> subcategoryMatches = Stream.concat(subcategory1Matches.stream(), subcategory2Matches.stream()).toList();
        return subcategoryMatches;
    }

    public Category updateCategory(Long id, Category updatedCategory) {
        Optional<Category> existingCategory = categoryRepository.findById(id);

        if (existingCategory.isPresent()) {
            Category category = existingCategory.get();
            category.setShortName(updatedCategory.getShortName());
            category.setSubcategory1(updatedCategory.getSubcategory1());
            category.setSubcategory2(updatedCategory.getSubcategory2());
            return categoryRepository.save(category);
        } else {
            throw new RuntimeException("Category not found with ID: " + id);
        }
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
