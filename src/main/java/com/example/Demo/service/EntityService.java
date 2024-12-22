package com.example.Demo.service;

import com.example.Demo.Exception.ValidationException;
import com.example.Demo.model.common.PageResponse;
import com.example.Demo.model.dao.Entity;
import com.example.Demo.model.dto.EntityDTO;

import java.util.Optional;

public interface EntityService {
    Entity addEntity(EntityDTO entityDTO) throws ValidationException;

    Optional<Entity> getEntity(Long id);

    PageResponse getAllEntity(int pageNo, int pageSize, String name, String sortBy, String entityType);

    void updateEntity(Long id, EntityDTO entityDTO);

    void deleteEntityById(Long id);
}
