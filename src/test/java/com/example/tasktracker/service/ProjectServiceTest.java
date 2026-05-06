package com.example.tasktracker.service;

import com.example.tasktracker.dto.response.ProjectResponse;
import com.example.tasktracker.entity.Project;
import com.example.tasktracker.exception.ResourceNotFoundException;
import com.example.tasktracker.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository repository;

    @InjectMocks
    private ProjectService service;

    @Test
    void shouldGetProjectById() {
        Project project = new Project();
        project.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(project));

        ProjectResponse result = service.getById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void shouldThrowWhenProjectNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getById(1L));
    }

    @Test
    void shouldDeleteProject() {
        service.delete(1L);

        verify(repository).deleteById(1L);
    }
}