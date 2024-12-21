package com.example.Demo.controller;

import com.example.Demo.model.common.PageResponse;
import com.example.Demo.model.dao.Entity;
import com.example.Demo.model.dto.EntityDTO;
import com.example.Demo.service.EntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

@WebMvcTest(EntityController.class)
class EntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EntityService entityService;  // Use @MockBean here

    private EntityDTO entityDTO;
    private Entity entity;

    @BeforeEach
    void setUp() {
        entityDTO = new EntityDTO();
        entityDTO.setName("Admin User");
        entityDTO.setDescription("User");
        entityDTO.setEntityType(Entity.EntityType.USER);

        entity = new Entity();
        entity.setEntityId(1L);
        entity.setName("Admin User");
        entity.setDescription("User");
        entity.setEntityType(Entity.EntityType.USER);
    }

    @Test
    void addEntity() throws Exception {
        when(entityService.addEntity(any(EntityDTO.class))).thenReturn(entity);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Admin User\", \"description\":\"User\", \"entityType\":\"USER\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Admin User"))
                .andExpect(jsonPath("$.description").value("User"));

        verify(entityService, times(1)).addEntity(any(EntityDTO.class));
    }

    @Test
    void getEntityById() throws Exception {
        when(entityService.getEntity(1L)).thenReturn(Optional.of(entity));

        mockMvc.perform(MockMvcRequestBuilders.get("/user/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Admin User"))
                .andExpect(jsonPath("$.description").value("User"));

        verify(entityService, times(1)).getEntity(1L);
    }

    @Test
    void getEntityById_NotFound() throws Exception {
        when(entityService.getEntity(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/user/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("Entity not found with id: 1"));

        verify(entityService, times(1)).getEntity(1L);
    }


    @Test
    void getAllEntities() throws Exception {
        // Create a mock Entity object with values
        Entity entity = new Entity();
        entity.setName("Admin User");
        entity.setDescription("User");
        entity.setEntityType(Entity.EntityType.USER);

        // Create the PageResponse and set mock data
        PageResponse pageResponse = new PageResponse();
        pageResponse.setData(List.of(entity));  // Correctly set data field

        // Mock the service layer method
        when(entityService.getAllEntity(0, 10, null, "createdAt", "USER")).thenReturn(pageResponse);

        // Perform the GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/user/")
                        .param("pageNo", "0")
                        .param("pageSize", "10")
                        .param("sortBy", "createdAt")
                        .param("entityType", "USER"))  // Pass entityType as a request parameter
                .andExpect(status().isOk())  // Expecting HTTP 200
                .andExpect(jsonPath("$.data[0].name").value("Admin User"))
                .andExpect(jsonPath("$.data[0].description").value("User"));

        // Verify the interaction with the service layer
        verify(entityService, times(1)).getAllEntity(0, 10, null, "createdAt", "USER");
    }


    @Test
    void getAllEntitiesWithNameFilter() throws Exception {
        // Create a mock Entity object with values
        Entity entity = new Entity();
        entity.setName("Admin User");
        entity.setDescription("User");
        entity.setEntityType(Entity.EntityType.USER);

        // Create the PageResponse and set mock data
        PageResponse pageResponse = new PageResponse();
        pageResponse.setData(List.of(entity));

        // Mock the service layer method
        when(entityService.getAllEntity(0, 10, "Admin", "createdAt", "USER")).thenReturn(pageResponse);

        // Perform the GET request with the name filter
        mockMvc.perform(MockMvcRequestBuilders.get("/user/")
                        .param("pageNo", "0")
                        .param("pageSize", "10")
                        .param("name", "Admin")  // Pass name as a request parameter
                        .param("sortBy", "createdAt")
                        .param("entityType", "USER"))  // Pass entityType as a request parameter
                .andExpect(status().isOk())  // Expecting HTTP 200
                .andExpect(jsonPath("$.data[0].name").value("Admin User"))
                .andExpect(jsonPath("$.data[0].description").value("User"));

        // Verify the interaction with the service layer
        verify(entityService, times(1)).getAllEntity(0, 10, "Admin", "createdAt", "USER");
    }


    @Test
    void updateEntity() throws Exception {
        doNothing().when(entityService).updateEntity(eq(1L), any(EntityDTO.class));
        mockMvc.perform(MockMvcRequestBuilders.put("/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Admin Updated\", \"description\":\"Updated User\", \"entityType\":\"USER\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Entity updated successfully"));

        verify(entityService, times(1)).updateEntity(eq(1L), any(EntityDTO.class));
    }

    @Test
    void deleteEntity() throws Exception {
        doNothing().when(entityService).deleteEntityById(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("Entity deleted successfully"));

        verify(entityService, times(1)).deleteEntityById(1L);
    }
}
