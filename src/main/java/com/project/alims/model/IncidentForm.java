package com.project.alims.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "incident_forms")
public class IncidentForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "incident_form_id")
    private Long incidentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

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

    @Column(name = "attachments")
    private String attachments;

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

    public IncidentForm(Long incidentId, User user, LocalDate date, LocalTime time, String natureOfIncident, String materialsInvolved, String involvedIndividuals, String attachments, LocalDateTime creationDate, LocalDateTime dateUpdated) {
        this.incidentId = incidentId;
        this.user = user;
        this.date = date;
        this.time = time;
        this.natureOfIncident = natureOfIncident;
        this.materialsInvolved = materialsInvolved;
        this.involvedIndividuals = involvedIndividuals;
        this.attachments = attachments;
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
}
