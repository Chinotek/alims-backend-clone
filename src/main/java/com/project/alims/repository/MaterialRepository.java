package com.project.alims.repository;

import com.project.alims.model.Material;
import com.project.alims.model.Category;
import com.project.alims.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findByCategory(Category category);
    List<Material> findBySupplier(Supplier supplier);
    Material findByItemCode(String itemCode);
}
