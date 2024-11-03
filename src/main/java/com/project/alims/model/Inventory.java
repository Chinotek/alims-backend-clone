package com.project.alims.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Long inventoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_code", referencedColumnName = "material_id", nullable = false)
    private Material material;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_log_id", referencedColumnName = "inventory_log_id", nullable = false)
    private InventoryLog inventoryLog;

    @Column(name = "qty", nullable = false)
    private Integer qty;

    @Column(name = "unit")
    private String unit;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @Column(name = "date_updated", nullable = false)
    private LocalDateTime dateUpdated;

    @PrePersist
    protected void onCreate() {
        creationDate = LocalDateTime.now();
        dateUpdated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dateUpdated = LocalDateTime.now();
    }

    // Constructors, Getters, and Setters
    public Inventory() {}

    public Inventory(Long inventoryId, Material material, InventoryLog inventoryLog, Integer qty, String unit, LocalDateTime creationDate, LocalDateTime dateUpdated) {
        this.inventoryId = inventoryId;
        this.material = material;
        this.inventoryLog = inventoryLog;
        this.qty = qty;
        this.unit = unit;
        this.creationDate = creationDate;
        this.dateUpdated = dateUpdated;
    }

    public Long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Long inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public InventoryLog getInventoryLog() {
        return inventoryLog;
    }

    public void setInventoryLog(InventoryLog inventoryLog) {
        this.inventoryLog = inventoryLog;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(LocalDateTime dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
}
