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

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "item_code", nullable = false)
    private Long itemCode;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    @Column(name = "nature_of_incident", nullable = false)
    private String natureOfIncident;

    @Column(name = "materials_involved")
    private String materialsInvolved;

    @Column(name = "qty")
    private Integer qty;

    @Column(name = "brand")
    private String brand;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "involved_individuals")
    private String involvedIndividuals;

    @Column(name = "attached_documentation")
    private String attachedDocumentation;

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

    public Incident(Long userId, Long itemCode, LocalDate date, LocalTime time, String natureOfIncident, String materialsInvolved, Integer qty, String brand, String remarks, String involvedIndividuals, String attachedDocumentation) {
        this.userId = userId;
        this.itemCode = itemCode;
        this.date = date;
        this.time = time;
        this.natureOfIncident = natureOfIncident;
        this.materialsInvolved = materialsInvolved;
        this.qty = qty;
        this.brand = brand;
        this.remarks = remarks;
        this.involvedIndividuals = involvedIndividuals;
        this.attachedDocumentation = attachedDocumentation;
    }

    // Getters and Setters
    public Long getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(Long incidentId) {
        this.incidentId = incidentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getItemCode() {
        return itemCode;
    }

    public void setItemCode(Long itemCode) {
        this.itemCode = itemCode;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getNatureOfIncident() {
        return natureOfIncident;
    }

    public void setNatureOfIncident(String natureOfIncident) {
        this.natureOfIncident = natureOfIncident;
    }

    public String getMaterialsInvolved() {
        return materialsInvolved;
    }

    public void setMaterialsInvolved(String materialsInvolved) {
        this.materialsInvolved = materialsInvolved;
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

    public String getInvolvedIndividuals() {
        return involvedIndividuals;
    }

    public void setInvolvedIndividuals(String involvedIndividuals) {
        this.involvedIndividuals = involvedIndividuals;
    }

    public String getAttachedDocumentation() {
        return attachedDocumentation;
    }

    public void setAttachedDocumentation(String attachedDocumentation) {
        this.attachedDocumentation = attachedDocumentation;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getDateUpdated() {
        return dateUpdated;
    }
}
