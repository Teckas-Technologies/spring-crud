package com.example.Demo.controller;

import com.example.Demo.Exception.EntityNotFoundException;
import com.example.Demo.Exception.ValidationException;
import com.example.Demo.model.common.PageResponse;
import com.example.Demo.model.dao.Entity;
import com.example.Demo.model.dto.EntityDTO;
import com.example.Demo.service.EntityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/entities")
@Tag(name = "Entity Management", description = "APIs for managing entities")
public class EntityController {

    private  static  final Logger logger = LoggerFactory.getLogger(EntityController.class);
    @Autowired
    private EntityService entityService;

    /**
     *Endpoint to add a new entity
     * @param entityDTO -  The details of the entity to be created.
     * @return ResponseEntity containing the created entity.
     * @throws ValidationException ValidationException if the provided data is invalid.
     */

    @Operation(summary = "Add a new entity", description = "Creates a new user in the system")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "entity created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/")
    public ResponseEntity<?> addEntity(@RequestBody EntityDTO entityDTO)throws ValidationException {
        logger.info("Received request to add Entity: {}", entityDTO);
        Entity entity = entityService.addEntity(entityDTO);
        logger.info("Entity created successfully");
        return new ResponseEntity<>(entity,HttpStatus.CREATED);
    }

    /**
     * Endpoint to fetch an entity by its ID.
     * @param id  The unique ID of the entity.
     * @return ResponseEntity containing the entity details.
     */
    @Operation(summary = "Get Entity by ID", description = "Fetches Entity details by their unique ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Entity found"),
            @ApiResponse(responseCode = "404", description = "Entity not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getEntityById(@PathVariable Long id) {
        logger.info("Received request to get Entity with id: {}", id);
        Entity entity = entityService.getEntity(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    /**
     *  Endpoint to fetch all entities with optional filters and pagination.
     * @param pageNo Page number for pagination (default: 0).
     * @param pageSize Number of records per page (default: 10).
     * @param name Optional filter by name.
     * @param sortBy Field to sort the results (default: createdAt).
     * @param entityType Optional filter by entity type.
     * @return ResponseEntity containing a paginated list of entities.
     */

    @Operation(summary = "Get all Entities", description = "Fetches a paginated list of all Entity")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Entities fetched successfully")
    })
    @GetMapping("/")
    public ResponseEntity<?> getAllEntities(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false) String entityType) {
        logger.info("Received request to get all Entities with pageNo: {}, pageSize: {}, name: {}, sortBy: {}, entityType: {}", pageNo, pageSize, name, sortBy, entityType);
        PageResponse user = entityService.getAllEntity(pageNo, pageSize, name, sortBy, entityType);
        logger.info("Fetched {} Entities", user.getData().size());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Endpoint to update an entity's details.
     * @param id The unique ID of the entity to be updated.
     * @param entityDTO The new details of the entity.
     * @return ResponseEntity with a success message.
     * @throws ValidationException ValidationException if the provided data is invalid.
     */

    @Operation(summary = "Update a Entity", description = "Updates Entity details for the given ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Entity updated successfully"),
            @ApiResponse(responseCode = "404", description = "Entity not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEntity(@PathVariable Long id,@RequestBody EntityDTO entityDTO)throws ValidationException {
        logger.info("Received request to update Entity with id: {}", id);
        entityService.updateEntity(id, entityDTO);
        logger.info("Entity with id {} updated successfully", id);
        return new ResponseEntity<>("Entity updated successfully",HttpStatus.OK);
    }

    /**
     * Endpoint to delete an entity.
     * @param id The unique ID of the entity to be deleted.
     * @return ResponseEntity with a success message.
     */
    @Operation(summary = "Delete a Entity", description = "Deletes a Entity for the given ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Entity deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Entity not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEntity(@PathVariable Long id){
        logger.info("Received request to delete user with id: {}", id);
        entityService.deleteEntityById(id);
        logger.info("Entity with id {} deleted successfully", id);
        return new ResponseEntity<>("Entity deleted successfully",HttpStatus.OK);
    }
}
