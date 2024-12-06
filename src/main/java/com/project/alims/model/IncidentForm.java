package com.project.alims.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "incident_forms")
public class IncidentForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "incident_form_id")
    private Long incidentFormId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    @Column(name = "nature_of_incident", nullable = false)
    private String natureOfIncident;

    @Column(name = "materials_involved")
    private String materialsInvolved;

    @Column(name = "involved_individuals")
    private String involvedIndividuals;

    @Column(name = "material_id")
    private String materialId; // comma separated based on amount of materials

    @Column(name = "user_id")
    private String userId; // comma separated based on amount of users

    @Column(name = "qty")
    private String qty; // comma separated based on amount of materials

    @Column(name = "brand")
    private String brand;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "attachments")
    private String attachments;

    @Column()
    private String fileType;  // store filesystem path

    @Column()
    @ElementCollection
    private List<byte[]> files;

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
    public IncidentForm() {}

    public IncidentForm(Long incidentFormId, LocalDate date, LocalTime time, String natureOfIncident, String materialsInvolved, String involvedIndividuals, String materialId, String userId, String qty, String brand, String remarks) {
        this.incidentFormId = incidentFormId;
        this.date = date;
        this.time = time;
        this.natureOfIncident = natureOfIncident;
        this.materialsInvolved = materialsInvolved;
        this.involvedIndividuals = involvedIndividuals;
        this.materialId = materialId;
        this.userId = userId;
        this.qty = qty;
        this.brand = brand;
        this.remarks = remarks;
    }

    public IncidentForm(Long incidentFormId, LocalDate date, LocalTime time, String natureOfIncident, String materialsInvolved, String involvedIndividuals, String materialId, String userId, String qty, String brand, String remarks, String attachments, String fileType, List<byte[]> files) {
        this.incidentFormId = incidentFormId;
        this.date = date;
        this.time = time;
        this.natureOfIncident = natureOfIncident;
        this.materialsInvolved = materialsInvolved;
        this.involvedIndividuals = involvedIndividuals;
        this.materialId = materialId;
        this.userId = userId;
        this.qty = qty;
        this.brand = brand;
        this.remarks = remarks;
        this.attachments = attachments;
        this.fileType = fileType;
        this.files = files;
    }

    // Getters and Setters
    public Long getIncidentFormId() {
        return incidentFormId;
    }

    public void setIncidentFormId(Long incidentFormId) {
        this.incidentFormId = incidentFormId;
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

    public String getInvolvedIndividuals() {
        return involvedIndividuals;
    }

    public void setInvolvedIndividuals(String involvedIndividuals) {
        this.involvedIndividuals = involvedIndividuals;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
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

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public List<byte[]> getFiles() {
        return files;
    }

    public void setFiles(List<byte[]> files) {
        this.files = files;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
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

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
