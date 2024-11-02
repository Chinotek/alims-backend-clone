package com.project.alims.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "incidents")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "incident_id")
    private Long incidentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_code", referencedColumnName = "id", nullable = false)
    private Material material;  // Foreign key to Materials

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", referencedColumnName = "incident_id", nullable = false)
    private IncidentForm incidentForm;  // Foreign key to Incident Form

    @Column(name = "qty")
    private Integer qty;

    @Column(name = "brand")
    private String brand;

    @Column(name = "remarks")
    private String remarks;

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
    public Incident() {}

    public Incident(Long incidentId, Material material, IncidentForm incidentForm, Integer qty, String brand, String remarks, LocalDateTime creationDate, LocalDateTime dateUpdated) {
        this.incidentId = incidentId;
        this.material = material;
        this.incidentForm = incidentForm;
        this.qty = qty;
        this.brand = brand;
        this.remarks = remarks;
        this.creationDate = creationDate;
        this.dateUpdated = dateUpdated;
    }

    // Getters and Setters


    public Long getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(Long incidentId) {
        this.incidentId = incidentId;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public IncidentForm getIncidentForm() {
        return incidentForm;
    }

    public void setIncidentForm(IncidentForm incidentForm) {
        this.incidentForm = incidentForm;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
