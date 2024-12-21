package com.example.Demo.service.impl;

import com.example.Demo.model.common.PageResponse;
import com.example.Demo.model.dao.User;
import com.example.Demo.model.dto.UserDTO;
import com.example.Demo.repository.UserRepository;
import com.example.Demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Override
    public User addUser(UserDTO userDTO) {
           logger.info("Adding user with email: {}", userDTO.getEmail());
            User user = new User();
            user.setEmail(userDTO.getEmail());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            userRepository.save(user);
            logger.info("User added successfully with email: {}", userDTO.getEmail());
        return user;
    }

    @Override
    public Optional<User> getUser(Long id) {
        logger.info("Fetching user with ID: {}", id);
        return userRepository.findById(id);
    }

    @Override
    public PageResponse getAllUsers(int pageNo, int pageSize) {
        logger.info("Fetching users with pageNo: {} and pageSize: {}", pageNo, pageSize);
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        Page<User> users = userRepository.findAll(pageRequest);
        PageResponse pageResponse = new PageResponse();
        pageResponse.setData(users.toList());
        pageResponse.setPageNumber(pageNo);
        pageResponse.setPageSize(pageSize);
        pageResponse.setTotalPages(users.getTotalPages());
        logger.info("Fetched {} users on page {} of size {}", users.getTotalElements(), pageNo, pageSize);
        return pageResponse;
    }

    @Override
    public void updateUser(Long id, UserDTO userDTO) {
        logger.info("Updating user with ID: {}", id);
        User user = userRepository.getReferenceById(id);
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        userRepository.save(user);
        logger.info("User with ID: {} updated successfully", id);
    }

    @Override
    public void deleteUserById(Long id) {
        logger.info("Attempting to delete user with ID: {}", id);

        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            userRepository.deleteById(id);
            logger.info("User with ID {} deleted successfully", id);
        } else {
            logger.warn("User with ID {} not found", id);
            throw new IllegalArgumentException("User not found with ID: " + id);  // Throwing a standard exception
        }
    }

}
