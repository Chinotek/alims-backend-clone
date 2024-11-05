package com.project.alims.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_logs")
public class InventoryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_log_id")
    private Long inventoryLogId;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "material_id")
    private Long materialId;

    @ManyToOne()
    @JoinColumn(name = "material_id", referencedColumnName = "material_id", insertable = false, updatable = false)
    private Material material;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "source")
    private String source;

    @Column(name = "remarks")
    private String remarks;

    public InventoryLog() {}

    public InventoryLog(Long inventoryLogId, Long userId, User user, Long materialId, Material material, LocalDate date, Integer quantity, String source, String remarks) {
        this.inventoryLogId = inventoryLogId;
        this.userId = userId;
        this.user = user;
        this.materialId = materialId;
        this.material = material;
        this.date = date;
        this.quantity = quantity;
        this.source = source;
        this.remarks = remarks;
    }

    public Long getInventoryLogId() {
        return inventoryLogId;
    }

    public void setInventoryLogId(Long inventoryLogId) {
        this.inventoryLogId = inventoryLogId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
