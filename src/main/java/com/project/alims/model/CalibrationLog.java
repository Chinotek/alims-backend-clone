package com.project.alims.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "calibration_logs")
public class CalibrationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calibration_id")
    private Long calibrationId;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;  // Foreign key to User model

    @Column(name = "material_id")
    private Long materialId;

    @ManyToOne()
    @JoinColumn(name = "material_id", referencedColumnName = "material_id", insertable = false, updatable = false)
    private Material material;  // Foreign key to Material model

    @Column(name = "calibration_date", nullable = false)
    private LocalDate calibrationDate;

    @Column(name = "next_calibration", nullable = false)
    private LocalDate nextCalibration;

    @Column(length = 1000)
    private String notes;

    @Column(length = 500)
    private String attachments;  // store filesystem path

    @Column()
    private String fileType;

    @Column()
    private byte[] file;

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

    public CalibrationLog(Long calibrationId, Long userId, User user, Long materialId, Material material, LocalDate calibrationDate, LocalDate nextCalibration, String notes, String attachments) {
        this.calibrationId = calibrationId;
        this.userId = userId;
        this.user = user;
        this.materialId = materialId;
        this.material = material;
        this.calibrationDate = calibrationDate;
        this.nextCalibration = nextCalibration;
        this.notes = notes;
        this.attachments = attachments;
    }

    public CalibrationLog(Long calibrationId, Long userId, User user, Long materialId, Material material, LocalDate calibrationDate, LocalDate nextCalibration, String notes, String attachments, String fileType, byte[] file) {
        this.calibrationId = calibrationId;
        this.userId = userId;
        this.user = user;
        this.materialId = materialId;
        this.material = material;
        this.calibrationDate = calibrationDate;
        this.nextCalibration = nextCalibration;
        this.notes = notes;
        this.attachments = attachments;
        this.fileType = fileType;
        this.file = file;
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

    public LocalDate getCalibrationDate() {
        return calibrationDate;
    }

    public void setCalibrationDate(LocalDate calibrationDate) {
        this.calibrationDate = calibrationDate;
    }

    public LocalDate getNextCalibration() {
        return nextCalibration;
    }

    public void setNextCalibration(LocalDate nextCalibration) {
        this.nextCalibration = nextCalibration;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setDateUpdated(LocalDateTime dateUpdated) {
        this.dateUpdated = dateUpdated;
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

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
