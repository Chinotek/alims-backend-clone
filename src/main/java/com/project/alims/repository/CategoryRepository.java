package com.project.alims.repository;

import com.project.alims.model.Category;
import com.project.alims.model.Laboratory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByLaboratory(Laboratory lab);

    Category findByShortName(String shortName);

    Category findBySubcategory(String subcategory);

    List<Category> findByCreatedAtAfter(LocalDateTime dateTime);

    List<Category> findByUpdatedAtBefore(LocalDateTime dateTime);
}
