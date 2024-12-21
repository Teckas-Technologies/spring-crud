package com.example.Demo.controller;

import com.example.Demo.model.common.PageResponse;
import com.example.Demo.model.dao.User;
import com.example.Demo.model.dto.UserDTO;
import com.example.Demo.service.UserService;
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

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;  // Use @MockBean here

    private UserDTO userDTO;
    private User user;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setEmail("admin@example.com");
        userDTO.setFirstName("Admin User");
        userDTO.setLastName("User");

        user = new User();
        user.setUserId(1L);
        user.setEmail("admin@example.com");
        user.setFirstName("Admin User");
        user.setLastName("User");
    }

    @Test
    void addUsers() throws Exception {
        when(userService.addUser(any(UserDTO.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"admin@example.com\", \"name\":\"Admin User\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("admin@example.com"))
                .andExpect(jsonPath("$.firstName").value("Admin User"))
                .andExpect(jsonPath("$.lastName").value("User"));

        verify(userService, times(1)).addUser(any(UserDTO.class));
    }


    @Test
    void getUserById() throws Exception {
        when(userService.getUser(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/user/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("admin@example.com"))
                .andExpect(jsonPath("$.firstName").value("Admin User"))
                .andExpect(jsonPath("$.lastName").value("User"));

        verify(userService, times(1)).getUser(1L);
    }


    @Test
    void getUserById_NotFound() throws Exception {
        when(userService.getUser(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/user/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));

        verify(userService, times(1)).getUser(1L);
    }

    @Test
    void getAllUsers() throws Exception {
        PageResponse pageResponse = new PageResponse();
        pageResponse.setData(List.of(user));

        when(userService.getAllUsers(0, 10)).thenReturn(pageResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/")
                        .param("pageNo", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].email").value("admin@example.com"))
                .andExpect(jsonPath("$.data[0].firstName").value("Admin User"))  // Corrected path
                .andExpect(jsonPath("$.data[0].lastName").value("User"));   // Corrected path

        verify(userService, times(1)).getAllUsers(0, 10);
    }

    @Test
    void updateUser() throws Exception {
        doNothing().when(userService).updateUser(eq(1L), any(UserDTO.class));
        mockMvc.perform(MockMvcRequestBuilders.put("/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"admin@example.com\", \"name\":\"Admin Updated\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("User updated successfully"));

        verify(userService, times(1)).updateUser(eq(1L), any(UserDTO.class));
    }

    @Test
    void deleteUser() throws Exception {
        doNothing().when(userService).deleteUserById(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));

        verify(userService, times(1)).deleteUserById(1L);
    }
}
