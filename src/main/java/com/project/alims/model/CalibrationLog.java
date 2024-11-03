package com.project.alims.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "calibration_logs")
public class CalibrationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calibration_id")
    private Long calibrationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;  // Foreign key to User model

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_code", referencedColumnName = "material_id", nullable = false)
    private Material material;  // Foreign key to Material model

    @Column(name = "calibration_date", nullable = false)
    private LocalDateTime calibrationDate;

    @Column(name = "next_calibration", nullable = false)
    private LocalDateTime nextCalibration;

    @Column(length = 1000)
    private String notes;

    @Column(length = 500)
    private String attachments;  // store filesystem path

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

    // Default constructor
    public CalibrationLog() {
    }

    // Parameterized constructor
    public CalibrationLog(User user, Material material, LocalDateTime calibrationDate, LocalDateTime nextCalibration,
                          String notes, String attachments) {
        this.user = user;
        this.material = material;
        this.calibrationDate = calibrationDate;
        this.nextCalibration = nextCalibration;
        this.notes = notes;
        this.attachments = attachments;
    }

    // Getters and Setters

    public Long getCalibrationId() {
        return calibrationId;
    }

    public void setCalibrationId(Long calibrationId) {
        this.calibrationId = calibrationId;
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

    public LocalDateTime getCalibrationDate() {
        return calibrationDate;
    }

    public void setCalibrationDate(LocalDateTime calibrationDate) {
        this.calibrationDate = calibrationDate;
    }

    public LocalDateTime getNextCalibration() {
        return nextCalibration;
    }

    public void setNextCalibration(LocalDateTime nextCalibration) {
        this.nextCalibration = nextCalibration;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public LocalDateTime getDateUpdated() {
        return dateUpdated;
    }
}
