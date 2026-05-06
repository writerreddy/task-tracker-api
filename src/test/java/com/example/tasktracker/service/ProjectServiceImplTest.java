package com.example.tasktracker.service;

import com.example.tasktracker.dto.request.ProjectRequest;
import com.example.tasktracker.dto.response.ProjectResponse;
import com.example.tasktracker.entity.Project;
import com.example.tasktracker.exception.DuplicateResourceException;
import com.example.tasktracker.exception.ResourceNotFoundException;
import com.example.tasktracker.repository.ProjectRepository;
import com.example.tasktracker.repository.TaskRepository;
import com.example.tasktracker.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private Project project;
    private ProjectRequest request;

    @BeforeEach
    void setUp() {
        project = new Project();
        project.setId(1L);
        project.setName("Media Platform");
        project.setDescription("Backend services");
        project.setCreatedAt(LocalDateTime.now());

        request = new ProjectRequest();
        request.setName("Media Platform");
        request.setDescription("Backend services");
    }

    @Test
    void shouldCreateProject() {
        when(projectRepository.existsByName(request.getName())).thenReturn(false);
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectResponse response = projectService.create(request);

        assertNotNull(response);
        assertEquals("Media Platform", response.getName());
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void shouldThrowExceptionWhenProjectNameAlreadyExists() {
        when(projectRepository.existsByName(request.getName())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, new org.junit.jupiter.api.function.Executable() {
            @Override
            public void execute() {
                projectService.create(request);
            }
        });
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    void shouldReturnAllProjects() {
        when(projectRepository.findAll()).thenReturn(Arrays.asList(project));

        assertEquals(1, projectService.getAll().size());
    }

    @Test
    void shouldReturnProjectById() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        ProjectResponse response = projectService.getById(1L);

        assertEquals(1L, response.getId());
    }

    @Test
    void shouldThrowWhenProjectNotFound() {
        when(projectRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, new org.junit.jupiter.api.function.Executable() {
            @Override
            public void execute() {
                projectService.getById(100L);
            }
        });
    }

    @Test
    void shouldUpdateProject() {
        ProjectRequest updateRequest = new ProjectRequest();
        updateRequest.setName("Updated Project");
        updateRequest.setDescription("Updated Description");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepository.existsByName("Updated Project")).thenReturn(false);
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProjectResponse response = projectService.update(1L, updateRequest);

        assertEquals("Updated Project", response.getName());
    }

    @Test
    void shouldDeleteProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        projectService.delete(1L);

        verify(projectRepository).delete(project);
    }
}
