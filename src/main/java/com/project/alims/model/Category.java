package com.project.alims.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "lab_id")
    private Long labId;

    @ManyToOne()
    @JoinColumn(name = "lab_id", referencedColumnName = "lab_id", insertable = false, updatable = false)
    private Laboratory laboratory;


    @Column(nullable = false, unique = true)
    private String shortName;

    @Column(nullable = false, unique = true)
    private String subcategory;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Category(){
    }

    public Category(Long categoryId, Long labId, Laboratory laboratory, String shortName, String subcategory, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.categoryId = categoryId;
        this.labId = labId;
        this.laboratory = laboratory;
        this.shortName = shortName;
        this.subcategory = subcategory;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getLabId() {
        return labId;
    }

    public void setLabId(Long labId) {
        this.labId = labId;
    }

    public Laboratory getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(Laboratory laboratory) {
        this.laboratory = laboratory;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

}
