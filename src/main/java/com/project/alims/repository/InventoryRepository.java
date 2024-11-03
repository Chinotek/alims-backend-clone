package com.project.alims.repository;

import com.project.alims.model.IncidentForm;
import com.project.alims.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findAllByInventoryLogId(Long inventoryLogId);
}
