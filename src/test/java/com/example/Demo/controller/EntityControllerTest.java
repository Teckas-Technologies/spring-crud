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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EntityController.class)
class EntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EntityService entityService;

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
    @WithMockUser(username = "admin", password = "admin123" ,roles = {"ADMIN"})
    void addEntity() throws Exception {
        when(entityService.addEntity(any(EntityDTO.class))).thenReturn(entity);

        mockMvc.perform(MockMvcRequestBuilders.post("/entities/")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Admin User\", \"description\":\"User\", \"entityType\":\"USER\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Admin User"))
                .andExpect(jsonPath("$.description").value("User"));

        verify(entityService, times(1)).addEntity(any(EntityDTO.class));
    }

    @Test
    @WithMockUser(username = "admin", password = "admin123",roles = {"ADMIN"})
    void getEntityById() throws Exception {
        when(entityService.getEntity(1L)).thenReturn(Optional.of(entity));

        mockMvc.perform(MockMvcRequestBuilders.get("/entities/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Admin User"))
                .andExpect(jsonPath("$.description").value("User"));

        verify(entityService, times(1)).getEntity(1L);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getEntityById_NotFound() throws Exception {
        when(entityService.getEntity(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/entities/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("Entity not found with id: 1"));

        verify(entityService, times(1)).getEntity(1L);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllEntities() throws Exception {
        PageResponse pageResponse = new PageResponse();
        pageResponse.setData(List.of(entity));

        when(entityService.getAllEntity(0, 10, null, "createdAt", "USER")).thenReturn(pageResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/entities/")
                        .param("pageNo", "0")
                        .param("pageSize", "10")
                        .param("sortBy", "createdAt")
                        .param("entityType", "USER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("Admin User"))
                .andExpect(jsonPath("$.data[0].description").value("User"));

        verify(entityService, times(1)).getAllEntity(0, 10, null, "createdAt", "USER");
    }

    @Test
    @WithMockUser(username = "admin",password = "admin123",roles = {"ADMIN"})
    void updateEntity() throws Exception {
        doNothing().when(entityService).updateEntity(eq(1L), any(EntityDTO.class));

        mockMvc.perform(MockMvcRequestBuilders.put("/entities/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Admin Updated\", \"description\":\"Updated User\", \"entityType\":\"USER\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Entity updated successfully"));

        verify(entityService, times(1)).updateEntity(eq(1L), any(EntityDTO.class));
    }

    @Test
    @WithMockUser(username = "admin",password = "admin123",roles = {"ADMIN"})
    void deleteEntity() throws Exception {
        doNothing().when(entityService).deleteEntityById(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/entities/{id}", 1L).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Entity deleted successfully"));

        verify(entityService, times(1)).deleteEntityById(1L);
    }
}
