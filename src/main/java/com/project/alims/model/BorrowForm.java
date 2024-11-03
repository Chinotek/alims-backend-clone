package com.project.alims.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "borrow_forms")
public class BorrowForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "borrow_id")
    private Long borrowId;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "material_id")
    private Long materialId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", referencedColumnName = "material_id", insertable = false, updatable = false)
    private Material material;

    @Column(name = "date_borrowed", nullable = false)
    private LocalDate dateBorrowed;

    @Column(name = "details_of_borrowed")
    private String detailsOfBorrowed;

    @Column(name = "equipment")
    private String equipment;

    @Column(name = "qty")
    private Integer qty;

    @Column(name = "unit")
    private String unit;

    @Column(name = "borrower_detail")
    private String borrowerDetail;

    @Column(name = "department")
    private String department;

    @Column(name = "time_borrowed")
    private LocalTime timeBorrowed;

    @Column(name = "date_returned")
    private LocalDate dateReturned;

    @Column(name = "time_returned")
    private LocalTime timeReturned;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "damage_materials")
    private String damageMaterials;

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

    // Constructors
    public BorrowForm() {}

    public BorrowForm(Long borrowId, Long userId, User user, Long materialId, Material material, LocalDate dateBorrowed, String detailsOfBorrowed, String equipment, Integer qty, String unit, String borrowerDetail, String department, LocalTime timeBorrowed, LocalDate dateReturned, LocalTime timeReturned, String remarks, String damageMaterials) {
        this.borrowId = borrowId;
        this.userId = userId;
        this.user = user;
        this.materialId = materialId;
        this.material = material;
        this.dateBorrowed = dateBorrowed;
        this.detailsOfBorrowed = detailsOfBorrowed;
        this.equipment = equipment;
        this.qty = qty;
        this.unit = unit;
        this.borrowerDetail = borrowerDetail;
        this.department = department;
        this.timeBorrowed = timeBorrowed;
        this.dateReturned = dateReturned;
        this.timeReturned = timeReturned;
        this.remarks = remarks;
        this.damageMaterials = damageMaterials;
    }

    // Getters and Setters
    public Long getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Long borrowId) {
        this.borrowId = borrowId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public LocalDate getDateBorrowed() {
        return dateBorrowed;
    }

    public void setDateBorrowed(LocalDate dateBorrowed) {
        this.dateBorrowed = dateBorrowed;
    }

    public String getDetailsOfBorrowed() {
        return detailsOfBorrowed;
    }

    public void setDetailsOfBorrowed(String detailsOfBorrowed) {
        this.detailsOfBorrowed = detailsOfBorrowed;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
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

    public String getBorrowerDetail() {
        return borrowerDetail;
    }

    public void setBorrowerDetail(String borrowerDetail) {
        this.borrowerDetail = borrowerDetail;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalTime getTimeBorrowed() {
        return timeBorrowed;
    }

    public void setTimeBorrowed(LocalTime timeBorrowed) {
        this.timeBorrowed = timeBorrowed;
    }

    public LocalDate getDateReturned() {
        return dateReturned;
    }

    public void setDateReturned(LocalDate dateReturned) {
        this.dateReturned = dateReturned;
    }

    public LocalTime getTimeReturned() {
        return timeReturned;
    }

    public void setTimeReturned(LocalTime timeReturned) {
        this.timeReturned = timeReturned;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDamageMaterials() {
        return damageMaterials;
    }

    public void setDamageMaterials(String damageMaterials) {
        this.damageMaterials = damageMaterials;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getDateUpdated() {
        return dateUpdated;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setDateUpdated(LocalDateTime dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }
}
