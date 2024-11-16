package com.project.alims.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "disposal_form")
public class DisposalForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "disposal_id")
    private Long disposalId;

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

    @Column(name = "item_description", nullable = false)
    private String itemDescription;

    @Column(nullable = false)
    private Integer qty;

    @Column(name = "reason_for_disposal", nullable = false)
    private String reasonForDisposal;

    @Column(name = "disposal_method", nullable = false)
    private String disposalMethod;

    @Column(name = "disposed_by", nullable = false)
    private String disposedBy;

    @Column(name = "date_disposed")
    private LocalDate dateDisposed;

    @Column
    private String comments;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @Column(name = "date_updated", nullable = false)
    private LocalDateTime dateUpdated;

    // Constructors
    public DisposalForm() {}

    public DisposalForm(Long disposalId, Long userId, User user, Long materialId, Material material, String itemDescription, Integer qty, String reasonForDisposal, String disposalMethod, String disposedBy, LocalDate dateDisposed, String comments) {
        this.disposalId = disposalId;
        this.userId = userId;
        this.user = user;
        this.materialId = materialId;
        this.material = material;
        this.itemDescription = itemDescription;
        this.qty = qty;
        this.reasonForDisposal = reasonForDisposal;
        this.disposalMethod = disposalMethod;
        this.disposedBy = disposedBy;
        this.dateDisposed = dateDisposed;
        this.comments = comments;
    }

    // Getters and Setters

    @PrePersist
    protected void onCreate() {
        creationDate = LocalDateTime.now();
        dateUpdated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dateUpdated = LocalDateTime.now();
    }

    public Long getDisposalId() {
        return disposalId;
    }

    public void setDisposalId(Long disposalId) {
        this.disposalId = disposalId;
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

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getReasonForDisposal() {
        return reasonForDisposal;
    }

    public void setReasonForDisposal(String reasonForDisposal) {
        this.reasonForDisposal = reasonForDisposal;
    }

    public String getDisposalMethod() {
        return disposalMethod;
    }

    public void setDisposalMethod(String disposalMethod) {
        this.disposalMethod = disposalMethod;
    }

    public String getDisposedBy() {
        return disposedBy;
    }

    public void setDisposedBy(String disposedBy) {
        this.disposedBy = disposedBy;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public LocalDate getDateDisposed() {
        return dateDisposed;
    }

    public void setDateDisposed(LocalDate dateDisposed) {
        this.dateDisposed = dateDisposed;
    }
}
