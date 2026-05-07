package com.example.tasktracker.service.impl;

import com.example.tasktracker.dto.request.UserRequest;
import com.example.tasktracker.dto.response.PagedResponse;
import com.example.tasktracker.dto.response.UserResponse;
import com.example.tasktracker.entity.AppUser;
import com.example.tasktracker.exception.DuplicateResourceException;
import com.example.tasktracker.exception.ResourceNotFoundException;
import com.example.tasktracker.repository.UserRepository;
import com.example.tasktracker.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service implementation for managing user operations.
 *
 * Handles user creation, retrieval,
 * update, and deletion.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Constructs a UserServiceImpl with required repository.
     *
     * @param userRepository repository for user persistence
     */
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates a new user.
     *
     * @param request user creation request
     * @return created user response
     */
    @Override
    public UserResponse create(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User with email '" + request.getEmail() + "' already exists");
        }
        AppUser user = new AppUser();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        return mapToResponse(userRepository.save(user));
    }

    /**
     * Retrieves all users.
     *
     * @return list of user responses
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAll() {
        List<UserResponse> responses = new ArrayList<UserResponse>();
        for (AppUser user : userRepository.findAll()) {
            responses.add(mapToResponse(user));
        }
        return responses;
    }

    /**
     * Retrieves paginated users.
     *
     * @param page page number
     * @param size page size
     * @param sortBy field to sort by
     * @param sortDir sorting direction
     * @return paginated user response
     */
    @Override
    @Transactional(readOnly = true)
    public PagedResponse<UserResponse> getAll(int page, int size, String sortBy, String sortDir) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Page<AppUser> result = userRepository.findAll(PageRequest.of(page, size, sort));
        List<UserResponse> content = new ArrayList<UserResponse>();
        for (AppUser user : result.getContent()) {
            content.add(mapToResponse(user));
        }
        return buildPage(content, result);
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id user identifier
     * @return user response
     */
    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        return mapToResponse(findUser(id));
    }

    /**
     * Updates an existing user.
     *
     * @param id user identifier
     * @param request updated user details
     * @return updated user response
     */
    @Override
    public UserResponse update(Long id, UserRequest request) {
        AppUser user = findUser(id);
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User with email '" + request.getEmail() + "' already exists");
        }
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        return mapToResponse(userRepository.save(user));
    }

    /**
     * Deletes a user by ID.
     *
     * @param id user identifier
     */
    @Override
    public void delete(Long id) {
        AppUser user = findUser(id);
        userRepository.delete(user);
    }

    /**
     * Finds a user by ID.
     *
     * @param id user identifier
     * @return user entity
     */
    private AppUser findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id));
    }

    /**
     * Maps user entity to response DTO.
     *
     * @param user user entity
     * @return user response
     */
    private UserResponse mapToResponse(AppUser user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        return response;
    }

    /**
     * Builds paginated user response.
     *
     * @param content user response content
     * @param page page metadata
     * @return paged response
     */
    private PagedResponse<UserResponse> buildPage(List<UserResponse> content, Page<AppUser> page) {
        PagedResponse<UserResponse> response = new PagedResponse<UserResponse>();
        response.setContent(content);
        response.setPage(page.getNumber());
        response.setSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setFirst(page.isFirst());
        response.setLast(page.isLast());
        return response;
    }
}