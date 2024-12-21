package com.example.Demo.model.dao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@jakarta.persistence.Entity
@Table(name = "entities")
public class Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long entityId;
    @NotBlank(message = "name cannot be blank")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "description cannot be blank")
    @Size(min = 1, max = 50, message = "description must be between 1 and 50 characters")
    private String description;

    @Enumerated(EnumType.STRING)
    @Schema(description = "The type of entity")
    private EntityType entityType;
    private LocalDate createdAt;
    private LocalDate updatedAt;


    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    @PrePersist
    protected void onCreate() {
        updatedAt = createdAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }
    public enum EntityType {
        USER,
        PRODUCT,
        ITEM
    }
}

