package com.example.tasktracker.service;

import com.example.tasktracker.dto.request.UserRequest;
import com.example.tasktracker.dto.response.UserResponse;
import com.example.tasktracker.entity.AppUser;
import com.example.tasktracker.enums.UserRole;
import com.example.tasktracker.exception.DuplicateResourceException;
import com.example.tasktracker.exception.ResourceNotFoundException;
import com.example.tasktracker.repository.UserRepository;
import com.example.tasktracker.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private AppUser user;
    private UserRequest request;

    /**
     * Creates reusable test data before every test.
     */
    @BeforeEach
    void setUp() {

        user = new AppUser();
        user.setId(1L);
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setRole(UserRole.DEVELOPER);

        request = new UserRequest();
        request.setName("Alice");
        request.setEmail("alice@example.com");
        request.setRole(UserRole.DEVELOPER);
    }

    /**
     * Verifies that a user is successfully created
     * when the email does not already exist.
     */
    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUser() {

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(userRepository.save(any(AppUser.class)))
                .thenReturn(user);

        UserResponse response = userService.create(request);

        assertNotNull(response);
        assertEquals("Alice", response.getName());
        assertEquals("alice@example.com", response.getEmail());
        assertEquals("DEVELOPER", response.getRole());

        verify(userRepository).save(any(AppUser.class));
    }

    /**
     * Verifies that duplicate emails are rejected.
     */
    @Test
    @DisplayName("Should throw exception when email already exists")
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(true);

        assertThrows(DuplicateResourceException.class, () ->
                userService.create(request));

        verify(userRepository, never()).save(any(AppUser.class));
    }

    /**
     * Verifies that all users are returned correctly.
     */
    @Test
    @DisplayName("Should return all users")
    void shouldReturnAllUsers() {

        when(userRepository.findAll())
                .thenReturn(Arrays.asList(user));

        assertEquals(1, userService.getAll().size());
    }

    /**
     * Verifies behavior when database contains no users.
     */
    @Test
    @DisplayName("Should return empty list when no users exist")
    void shouldReturnEmptyListWhenNoUsersExist() {

        when(userRepository.findAll())
                .thenReturn(Collections.emptyList());

        assertTrue(userService.getAll().isEmpty());
    }

    /**
     * Verifies fetching a user by valid ID.
     */
    @Test
    @DisplayName("Should return user by ID")
    void shouldReturnUserById() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        UserResponse response = userService.getById(1L);

        assertEquals("Alice", response.getName());
        assertEquals("alice@example.com", response.getEmail());
    }

    /**
     * Verifies exception when user ID is not found.
     */
    @Test
    @DisplayName("Should throw exception when user not found")
    void shouldThrowWhenUserNotFound() {

        when(userRepository.findById(55L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                userService.getById(55L));
    }

    /**
     * Verifies successful user update.
     */
    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUser() {

        UserRequest updateRequest = new UserRequest();
        updateRequest.setName("Bob");
        updateRequest.setEmail("bob@example.com");
        updateRequest.setRole(UserRole.MANAGER);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(userRepository.existsByEmail("bob@example.com"))
                .thenReturn(false);

        when(userRepository.save(any(AppUser.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = userService.update(1L, updateRequest);

        assertEquals("Bob", response.getName());
        assertEquals("bob@example.com", response.getEmail());
        assertEquals("MANAGER", response.getRole());
    }

    /**
     * Verifies exception when updating a non-existing user.
     */
    @Test
    @DisplayName("Should throw exception when updating non-existing user")
    void shouldThrowWhenUpdatingNonExistingUser() {

        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                userService.update(999L, request));
    }

    /**
     * Verifies duplicate email validation during update.
     */
    @Test
    @DisplayName("Should throw exception when updating with duplicate email")
    void shouldThrowWhenUpdatingWithDuplicateEmail() {

        UserRequest updateRequest = new UserRequest();
        updateRequest.setName("Bob");
        updateRequest.setEmail("existing@example.com");
        updateRequest.setRole(UserRole.MANAGER);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(userRepository.existsByEmail("existing@example.com"))
                .thenReturn(true);

        assertThrows(DuplicateResourceException.class, () ->
                userService.update(1L, updateRequest));

        verify(userRepository, never()).save(any(AppUser.class));
    }

    /**
     * Verifies successful deletion of a user.
     */
    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUser() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        userService.delete(1L);

        verify(userRepository).delete(user);
    }

    /**
     * Verifies exception when deleting a non-existing user.
     */
    @Test
    @DisplayName("Should throw exception when deleting non-existing user")
    void shouldThrowWhenDeletingNonExistingUser() {

        when(userRepository.findById(404L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                userService.delete(404L));

        verify(userRepository, never()).delete(any(AppUser.class));
    }
}