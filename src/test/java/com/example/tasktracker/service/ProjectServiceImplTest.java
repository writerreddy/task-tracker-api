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

    @InjectMocks
    private ProjectServiceImpl projectService;

    private Project project;
    private ProjectRequest request;

    /**
     * Initializes reusable test data before each test execution.
     */
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

    /**
     * Verifies that a project is successfully created
     * when the project name does not already exist.
     */
    @Test
    void shouldCreateProject() {

        when(projectRepository.existsByName(request.getName()))
                .thenReturn(false);

        when(projectRepository.save(any(Project.class)))
                .thenReturn(project);

        ProjectResponse response = projectService.create(request);

        assertNotNull(response);
        assertEquals("Media Platform", response.getName());
        assertEquals("Backend services", response.getDescription());

        verify(projectRepository, times(1))
                .save(any(Project.class));
    }

    /**
     * Verifies that DuplicateResourceException is thrown
     * when attempting to create a project with an existing name.
     */
    @Test
    void shouldThrowExceptionWhenProjectNameAlreadyExists() {

        when(projectRepository.existsByName(request.getName()))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> projectService.create(request)
        );

        verify(projectRepository, never())
                .save(any(Project.class));
    }

    /**
     * Verifies that all projects are returned correctly.
     */
    @Test
    void shouldReturnAllProjects() {

        when(projectRepository.findAll())
                .thenReturn(Arrays.asList(project));

        assertEquals(1, projectService.getAll().size());

        verify(projectRepository, times(1))
                .findAll();
    }

    /**
     * Verifies behavior when the database contains no projects.
     */
    @Test
    void shouldReturnEmptyProjectList() {

        when(projectRepository.findAll())
                .thenReturn(Collections.emptyList());

        assertTrue(projectService.getAll().isEmpty());

        verify(projectRepository, times(1))
                .findAll();
    }

    /**
     * Verifies that a project can be fetched successfully by ID.
     */
    @Test
    void shouldReturnProjectById() {

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        ProjectResponse response = projectService.getById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Media Platform", response.getName());

        verify(projectRepository, times(1))
                .findById(1L);
    }

    /**
     * Verifies that ResourceNotFoundException is thrown
     * when the requested project does not exist.
     */
    @Test
    void shouldThrowWhenProjectNotFound() {

        when(projectRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> projectService.getById(100L)
        );

        verify(projectRepository, times(1))
                .findById(100L);
    }

    /**
     * Verifies that an existing project can be updated successfully.
     */
    @Test
    void shouldUpdateProject() {

        ProjectRequest updateRequest = new ProjectRequest();
        updateRequest.setName("Updated Project");
        updateRequest.setDescription("Updated Description");

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        when(projectRepository.existsByName("Updated Project"))
                .thenReturn(false);

        when(projectRepository.save(any(Project.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ProjectResponse response =
                projectService.update(1L, updateRequest);

        assertEquals("Updated Project", response.getName());
        assertEquals("Updated Description", response.getDescription());

        verify(projectRepository).save(any(Project.class));
    }

    /**
     * Verifies that update throws exception
     * when the target project does not exist.
     */
    @Test
    void shouldThrowWhenUpdatingNonExistingProject() {

        ProjectRequest updateRequest = new ProjectRequest();
        updateRequest.setName("New Name");

        when(projectRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> projectService.update(999L, updateRequest)
        );

        verify(projectRepository, never())
                .save(any(Project.class));
    }

    /**
     * Verifies that duplicate project names are not allowed during update.
     */
    @Test
    void shouldThrowWhenUpdatingWithDuplicateName() {

        ProjectRequest updateRequest = new ProjectRequest();
        updateRequest.setName("Existing Project");

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        when(projectRepository.existsByName("Existing Project"))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> projectService.update(1L, updateRequest)
        );

        verify(projectRepository, never())
                .save(any(Project.class));
    }

    /**
     * Verifies that a project can be deleted successfully.
     */
    @Test
    void shouldDeleteProject() {

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        projectService.delete(1L);

        verify(projectRepository, times(1))
                .delete(project);
    }

    /**
     * Verifies that delete operation throws exception
     * when the project does not exist.
     */
    @Test
    void shouldThrowWhenDeletingNonExistingProject() {

        when(projectRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> projectService.delete(999L)
        );

        verify(projectRepository, never())
                .delete(any(Project.class));
    }

    /**
     * Verifies that null descriptions are handled safely.
     */
    @Test
    void shouldHandleNullDescription() {

        project.setDescription(null);

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        ProjectResponse response = projectService.getById(1L);

        assertNotNull(response);
        assertNull(response.getDescription());
    }

    /**
     * Verifies that repository methods are called only once
     * for optimal service performance.
     */
    @Test
    void shouldCallRepositoryOnlyOnceForFindById() {

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        projectService.getById(1L);

        verify(projectRepository, only())
                .findById(1L);
    }
}
