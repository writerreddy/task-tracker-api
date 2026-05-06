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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
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

    @Test
    void shouldCreateUser() {
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.save(any(AppUser.class))).thenReturn(user);

        UserResponse response = userService.create(request);

        assertEquals("alice@example.com", response.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, new org.junit.jupiter.api.function.Executable() {
            @Override
            public void execute() {
                userService.create(request);
            }
        });
    }

    @Test
    void shouldReturnAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        assertEquals(1, userService.getAll().size());
    }

    @Test
    void shouldReturnUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertEquals("Alice", userService.getById(1L).getName());
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(userRepository.findById(55L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, new org.junit.jupiter.api.function.Executable() {
            @Override
            public void execute() {
                userService.getById(55L);
            }
        });
    }

    @Test
    void shouldUpdateUser() {
        UserRequest updateRequest = new UserRequest();
        updateRequest.setName("Bob");
        updateRequest.setEmail("bob@example.com");
        updateRequest.setRole(UserRole.MANAGER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("bob@example.com")).thenReturn(false);
        when(userRepository.save(any(AppUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = userService.update(1L, updateRequest);

        assertEquals("Bob", response.getName());
        assertEquals("MANAGER", response.getRole());
    }

    @Test
    void shouldDeleteUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.delete(1L);

        verify(userRepository).delete(user);
    }
}
