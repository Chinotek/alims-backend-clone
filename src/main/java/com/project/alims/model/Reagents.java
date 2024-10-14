package com.project.alims.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reagents")
public class Reagents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @Column(name = "qty", nullable = false)
    private Integer qty;

    @Column(name = "unit", nullable = false)
    private Integer unit;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Column(name = "cost", nullable = false)
    private Integer cost;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
