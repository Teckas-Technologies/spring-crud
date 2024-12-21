package com.example.Demo.service.impl;

import com.example.Demo.model.common.PageResponse;
import com.example.Demo.model.dao.Entity;
import com.example.Demo.model.dto.EntityDTO;
import com.example.Demo.repository.EntityRepository;
import com.example.Demo.service.EntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EntityServiceImpl implements EntityService {

    private static final Logger logger = LoggerFactory.getLogger(EntityServiceImpl.class);
    @Autowired
    private EntityRepository entityRepository;

    @Override
    public Entity addEntity(EntityDTO entityDTO) {
           logger.info("Adding entity");
            Entity entity = new Entity();
            entity.setName(entityDTO.getName());
            entity.setDescription(entityDTO.getDescription());
            entity.setEntityType(entityDTO.getEntityType());
            entityRepository.save(entity);
            logger.info("Entity added successfully");
        return entity;
    }

    @Override
    public Optional<Entity> getEntity(Long id) {
        logger.info("Fetching Entity with ID: {}", id);
        return entityRepository.findById(id);
    }

    @Override
    public PageResponse getAllEntity(int pageNo, int pageSize, String name, String sortBy, String entityType) {
        logger.info("Fetching Entities with pageNo: {}, pageSize: {}, name: {}, sortBy: {}, entityType: {}", pageNo, pageSize, name, sortBy, entityType);

        // Default sorting by createdAt if no sort parameter is passed
        Sort sort = Sort.by(Sort.Order.asc("createdAt"));
        if ("updatedAt".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(Sort.Order.asc("updatedAt"));
        }

        // Create Pageable instance with sorting
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        Page<Entity> entities;

        if (entityType != null && !entityType.isEmpty()) {
            // Filter by entityType if provided
            entities = entityRepository.findByEntityType(Entity.EntityType.valueOf(entityType), pageRequest);
        } else if (name != null && !name.isEmpty()) {
            // Filter by name if provided
            entities = entityRepository.findByNameContainingIgnoreCase(name, pageRequest);
        } else {
            // No filter, fetch all entities
            entities = entityRepository.findAll(pageRequest);
        }

        PageResponse pageResponse = new PageResponse();
        pageResponse.setData(entities.toList());
        pageResponse.setPageNumber(pageNo);
        pageResponse.setPageSize(pageSize);
        pageResponse.setTotalPages(entities.getTotalPages());

        logger.info("Fetched {} Entities on page {} with size {}", entities.getTotalElements(), pageNo, pageSize);
        return pageResponse;
    }



    @Override
    public void updateEntity(Long id, EntityDTO entityDTO) {
        logger.info("Updating Entity with ID: {}", id);
        Entity entity = entityRepository.getReferenceById(id);
        entity.setName(entityDTO.getName());
        entity.setDescription(entityDTO.getDescription());
        entity.setEntityType(entityDTO.getEntityType());
        entityRepository.save(entity);
        logger.info("Entity with ID: {} updated successfully", id);
    }

    @Override
    public void deleteEntityById(Long id) {
        logger.info("Attempting to delete Entity with ID: {}", id);

        Optional<Entity> user = entityRepository.findById(id);

        if (user.isPresent()) {
            entityRepository.deleteById(id);
            logger.info("Entity with ID {} deleted successfully", id);
        } else {
            logger.warn("Entity with ID {} not found", id);
            throw new IllegalArgumentException("Entity not found with ID: " + id);  // Throwing a standard exception
        }
    }

}