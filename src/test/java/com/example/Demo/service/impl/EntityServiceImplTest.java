package com.example.Demo.service.impl;

import com.example.Demo.Exception.ValidationException;
import com.example.Demo.model.common.PageResponse;
import com.example.Demo.model.dao.Entity;
import com.example.Demo.model.dto.EntityDTO;
import com.example.Demo.repository.EntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EntityServiceImplTest {

    @Mock
    private EntityRepository entityRepository;

    @InjectMocks
    private EntityServiceImpl userService;

    private EntityDTO entityDTO;
    private Entity entity;

    @BeforeEach
    void setUp() {
        // Prepare test data
        entityDTO = new EntityDTO("Test", "User", Entity.EntityType.USER);
        entity = new Entity();
        entity.setName(entityDTO.getName());
        entity.setDescription(entityDTO.getDescription());
        entity.setEntityType(entityDTO.getEntityType());
    }

    @Test
    void testAddEntity() throws ValidationException{
        when(entityRepository.save(any(Entity.class))).thenReturn(entity);

        Entity result = userService.addEntity(entityDTO);

        assertNotNull(result);
        assertEquals(entityDTO.getName(), result.getName());
        assertEquals(entityDTO.getDescription(), result.getDescription());
        assertEquals(entityDTO.getEntityType(), result.getEntityType());
        verify(entityRepository, times(1)).save(any(Entity.class)); // Verifying save was called once
    }

    @Test
    void testAddEntity_ValidationException() {
        // Simulating an invalid EntityDTO with null name
        EntityDTO invalidEntityDTO = new EntityDTO();
        invalidEntityDTO.setName(null); // This will trigger validation failure

        // Test that the ValidationException is thrown
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            userService.addEntity(invalidEntityDTO);
        });

        // Verifying the exception message
        assertEquals("Entity name cannot be null", exception.getMessage());

        // Optionally, verify that the repository save method was not called
        verify(entityRepository, times(0)).save(any(Entity.class)); // Verifying save was not called
    }


    @Test
    void testGetEntity_Found() {
        when(entityRepository.findById(anyLong())).thenReturn(Optional.of(entity));

        Optional<Entity> result = userService.getEntity(1L);

        assertTrue(result.isPresent());
        assertEquals(entity.getName(), result.get().getName());
        assertEquals(entity.getDescription(), result.get().getDescription());
        assertEquals(entity.getEntityType(), result.get().getEntityType());
        verify(entityRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetEntity_NotFound() {
        when(entityRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Entity> result = userService.getEntity(1L);

        assertFalse(result.isPresent());
        verify(entityRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetAllEntities() {
        // Given
        Page<Entity> entitiesPage = new PageImpl<>(java.util.List.of(entity));
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("createdAt")));
        when(entityRepository.findByEntityType(any(Entity.EntityType.class), any(PageRequest.class)))
                .thenReturn(entitiesPage);

        // When
        PageResponse result = userService.getAllEntity(0, 10, "", "createdAt", "USER");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getData().size()); // Assuming 1 entity returned
        assertEquals(1, result.getTotalPages()); // Assuming only 1 page of results
        verify(entityRepository, times(1)).findByEntityType(any(Entity.EntityType.class), any(PageRequest.class)); // Verifying the repository call
    }


    @Test
    void testGetAllEntitiesWithEntityTypeFilter() {
        // Given
        Page<Entity> entitiesPage = new PageImpl<>(java.util.List.of(entity));
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("createdAt")));
        when(entityRepository.findByEntityType(any(Entity.EntityType.class), any(PageRequest.class))).thenReturn(entitiesPage);

        // When
        PageResponse result = userService.getAllEntity(0, 10, null, "createdAt", "USER");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getData().size()); // Assuming 1 entity returned
        assertEquals(1, result.getTotalPages()); // Assuming only 1 page of results
        verify(entityRepository, times(1)).findByEntityType(any(Entity.EntityType.class), any(PageRequest.class)); // Verifying the repository call
    }


    @Test
    void testUpdateEntity() {
        when(entityRepository.getReferenceById(anyLong())).thenReturn(entity);
        when(entityRepository.save(any(Entity.class))).thenReturn(entity);

        userService.updateEntity(1L, entityDTO);

        assertEquals(entityDTO.getName(), entity.getName());
        assertEquals(entityDTO.getDescription(), entity.getDescription());
        assertEquals(entityDTO.getEntityType(), entity.getEntityType());
        verify(entityRepository, times(1)).getReferenceById(anyLong());
        verify(entityRepository, times(1)).save(any(Entity.class));
    }

    @Test
    void testDeleteEntityById_Found() {
        when(entityRepository.findById(anyLong())).thenReturn(Optional.of(entity));

        userService.deleteEntityById(1L);

        verify(entityRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testDeleteEntityById_NotFound() {
        when(entityRepository.findById(anyLong())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteEntityById(1L);
        });

        assertEquals("Entity not found with ID: 1", exception.getMessage());
        verify(entityRepository, times(1)).findById(anyLong());
    }
}
