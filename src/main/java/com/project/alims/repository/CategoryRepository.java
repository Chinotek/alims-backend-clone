package com.project.alims.repository;

import com.project.alims.model.Category;
import com.project.alims.model.Laboratory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByShortName(String shortName);
    List<Category> findBySubcategory1(String subcategory1);
    List<Category> findBySubcategory2(String subcategory1);

    Category findByCategoryId(Long categoryId);
}
