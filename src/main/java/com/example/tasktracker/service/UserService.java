package com.example.tasktracker.service;

import com.example.tasktracker.dto.request.UserRequest;
import com.example.tasktracker.dto.response.PagedResponse;
import com.example.tasktracker.dto.response.UserResponse;

import java.util.List;

/**
 * Service interface for user operations.
 *
 * Defines methods for managing users.
 */
public interface UserService {

    /**
     * Creates a new user.
     *
     * @param request user creation request
     * @return created user response
     */
    UserResponse create(UserRequest request);

    /**
     * Retrieves all users.
     *
     * @return list of user responses
     */
    List<UserResponse> getAll();

    /**
     * Retrieves paginated users.
     *
     * @param page page number
     * @param size page size
     * @param sortBy field to sort by
     * @param sortDir sorting direction
     * @return paginated user response
     */
    PagedResponse<UserResponse> getAll(int page, int size, String sortBy, String sortDir);

    /**
     * Retrieves a user by ID.
     *
     * @param id user identifier
     * @return user response
     */
    UserResponse getById(Long id);

    /**
     * Updates an existing user.
     *
     * @param id user identifier
     * @param request updated user details
     * @return updated user response
     */
    UserResponse update(Long id, UserRequest request);

    /**
     * Deletes a user by ID.
     *
     * @param id user identifier
     */
    void delete(Long id);
}