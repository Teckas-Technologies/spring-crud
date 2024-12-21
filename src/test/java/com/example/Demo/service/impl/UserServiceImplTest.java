package com.example.Demo.service.impl;

import com.example.Demo.model.common.PageResponse;
import com.example.Demo.model.dao.User;
import com.example.Demo.model.dto.UserDTO;
import com.example.Demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDTO userDTO;
    private User user;

    @BeforeEach
    void setUp() {
        // Prepare test data
        userDTO = new UserDTO("test@example.com", "Test", "User");
        user = new User();
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
    }

    @Test
    void testAddUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.addUser(userDTO);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class)); // Verifying save was called once
    }

    @Test
    void testGetUser_Found() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUser(1L);

        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetUser_NotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<User> result = userService.getUser(1L);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetAllUsers() {
        Page<User> usersPage = new PageImpl<>(java.util.List.of(user));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(usersPage);

        PageResponse result = userService.getAllUsers(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getData().size());
        assertEquals(1, result.getTotalPages());
        verify(userRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testUpdateUser() {
        when(userRepository.getReferenceById(anyLong())).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.updateUser(1L, userDTO);

        assertEquals(userDTO.getEmail(), user.getEmail());
        verify(userRepository, times(1)).getReferenceById(anyLong());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testDeleteUserById_Found() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userService.deleteUserById(1L);

        verify(userRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testDeleteUserById_NotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUserById(1L);
        });

        assertEquals("User not found with ID: 1", exception.getMessage());
        verify(userRepository, times(1)).findById(anyLong());
    }
}
