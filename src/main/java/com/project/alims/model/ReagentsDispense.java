package com.project.alims.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reagents_dispense")
public class ReagentsDispense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dispense_id")
    private Long dispenseId;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "reagent_id")
    private Long reagentId;

    @ManyToOne()
    @JoinColumn(name = "reagent_id", referencedColumnName = "material_id", insertable = false, updatable = false)
    private Reagents reagent;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "total_no_containers", nullable = false)
    private Integer totalNoContainers;

    @Column(name = "lot_no", nullable = false)
    private String lotNo;

    @Column(name = "qty_dispensed", nullable = false)
    private Double qtyDispensed;

    @Column(name = "remaining_qty", nullable = false)
    private Double remainingQty;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "analyst", nullable = false)
    private String analyst;

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
    public ReagentsDispense() {}

    public ReagentsDispense(Long dispenseId, Long userId, User user, Long reagentId, Reagents reagent, String name, LocalDate date, Integer totalNoContainers, String lotNo, Double qtyDispensed, Double remainingQty, String remarks, String analyst) {
        this.dispenseId = dispenseId;
        this.userId = userId;
        this.user = user;
        this.reagentId = reagentId;
        this.reagent = reagent;
        this.name = name;
        this.date = date;
        this.totalNoContainers = totalNoContainers;
        this.lotNo = lotNo;
        this.qtyDispensed = qtyDispensed;
        this.remainingQty = remainingQty;
        this.remarks = remarks;
        this.analyst = analyst;
    }

    // Getters and Setters
    public Long getDispenseId() {
        return dispenseId;
    }

    public void setDispenseId(Long dispenseId) {
        this.dispenseId = dispenseId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Reagents getReagent() {
        return reagent;
    }

    public void setReagent(Reagents reagent) {
        this.reagent = reagent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getTotalNoContainers() {
        return totalNoContainers;
    }

    public void setTotalNoContainers(Integer totalNoContainers) {
        this.totalNoContainers = totalNoContainers;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public Double getQtyDispensed() {
        return qtyDispensed;
    }

    public void setQtyDispensed(Double qtyDispensed) {
        this.qtyDispensed = qtyDispensed;
    }

    public Double getRemainingQty() {
        return remainingQty;
    }

    public void setRemainingQty(Double remainingQty) {
        this.remainingQty = remainingQty;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAnalyst() {
        return analyst;
    }

    public void setAnalyst(String analyst) {
        this.analyst = analyst;
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

    public Long getReagentId() {
        return reagentId;
    }

    public void setReagentId(Long reagentId) {
        this.reagentId = reagentId;
    }
}
