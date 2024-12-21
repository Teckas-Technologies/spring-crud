package com.example.Demo.service;

import com.example.Demo.model.common.PageResponse;
import com.example.Demo.model.dao.User;
import com.example.Demo.model.dto.UserDTO;

import java.util.Optional;

public interface UserService {
    User addUser(UserDTO userDTO);

    Optional<User> getUser(Long id);

    PageResponse getAllUsers(int pageNo, int pageSize);

    void updateUser(Long id, UserDTO userDTO);

    void deleteUserById(Long id);
}
