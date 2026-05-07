package com.example.tasktracker.controller;

import com.example.tasktracker.config.TaskProperties;
import com.example.tasktracker.dto.request.UserRequest;
import com.example.tasktracker.dto.response.UserResponse;
import com.example.tasktracker.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * REST controller for managing user-related operations.
 *
 * Provides endpoints for creating, retrieving,
 * updating, and deleting users.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final TaskProperties taskProperties;

    /**
     * Constructs a UserController with required dependencies.
     *
     * @param userService service for user operations
     * @param taskProperties pagination configuration properties
     */
    public UserController(UserService userService, TaskProperties taskProperties) {
        this.userService = userService;
        this.taskProperties = taskProperties;
    }

    /**
     * Creates a new user.
     *
     * @param request user creation request
     * @return created user details
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody UserRequest request) {
        return userService.create(request);
    }

    /**
     * Retrieves all users with optional pagination and sorting.
     *
     * @param page page number
     * @param size page size
     * @param sortBy field to sort by
     * @param sortDir sorting direction
     * @return list or paginated user response
     */
    @GetMapping
    public Object getAll(@RequestParam(required = false) Integer page,
                         @RequestParam(required = false) Integer size,
                         @RequestParam(defaultValue = "id") String sortBy,
                         @RequestParam(defaultValue = "asc") String sortDir) {
        if (page == null && size == null) {
            return userService.getAll();
        }
        int resolvedPage = page == null ? 0 : page.intValue();
        int resolvedSize = size == null ? taskProperties.getDefaultPageSize() : size.intValue();
        return userService.getAll(resolvedPage, resolvedSize, sortBy, sortDir);
    }

    /**
     * Retrieves a user by its ID.
     *
     * @param id user identifier
     * @return user details
     */
    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    /**
     * Updates an existing user.
     *
     * @param id user identifier
     * @param request updated user details
     * @return updated user response
     */
    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        return userService.update(id, request);
    }

    /**
     * Deletes a user by its ID.
     *
     * @param id user identifier
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
