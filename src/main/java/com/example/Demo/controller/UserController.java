package com.example.Demo.controller;

import com.example.Demo.Exception.UserNotFoundException;
import com.example.Demo.model.common.PageResponse;
import com.example.Demo.model.dao.User;
import com.example.Demo.model.dto.UserDTO;
import com.example.Demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    private  static  final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @Operation(summary = "Add a new user", description = "Creates a new user in the system")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/")
    public ResponseEntity<?> addUsers(@RequestBody UserDTO userDTO) {
        logger.info("Received request to add user: {}", userDTO);
        User user = userService.addUser(userDTO);
        logger.info("User created successfully: {}", userDTO.getEmail());
        return new ResponseEntity<>(user,HttpStatus.CREATED);
    }

    @Operation(summary = "Get user by ID", description = "Fetches user details by their unique ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        logger.info("Received request to get user with id: {}", id);
        User user = userService.getUser(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @Operation(summary = "Get all users", description = "Fetches a paginated list of all users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users fetched successfully")
    })
    @GetMapping("/")
    public ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize){
        logger.info("Received request to get all users with pageNo: {} and pageSize: {}", pageNo, pageSize);
        PageResponse user = userService.getAllUsers(pageNo,pageSize);
        logger.info("Fetched {} users", user.getData().size());
        return new ResponseEntity<>(user,HttpStatus.OK);
    }


    @Operation(summary = "Update a user", description = "Updates user details for the given ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,@RequestBody UserDTO userDTO){
        logger.info("Received request to update user with id: {}", id);
        userService.updateUser(id,userDTO);
        logger.info("User with id {} updated successfully", id);
        return new ResponseEntity<>("User updated successfully",HttpStatus.OK);
    }

    @Operation(summary = "Delete a user", description = "Deletes a user for the given ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        logger.info("Received request to delete user with id: {}", id);
        userService.deleteUserById(id);
        logger.info("User with id {} deleted successfully", id);
        return new ResponseEntity<>("User deleted successfully",HttpStatus.OK);
    }
}
