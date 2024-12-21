package com.example.Demo.model.dto;

import com.example.Demo.model.dao.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class EntityDTO {
    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private Entity.EntityType entityType;

    public Entity.EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(Entity.EntityType entityType) {
        this.entityType = entityType;
    }

    public EntityDTO(String name, String description,Entity.EntityType entityType) {
        this.name = name;
        this.description = description;
        this.entityType = entityType;
    }
    public EntityDTO() {
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

}
