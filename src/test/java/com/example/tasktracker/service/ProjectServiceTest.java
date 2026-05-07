package com.example.tasktracker.service;

import com.example.tasktracker.dto.response.ProjectResponse;
import com.example.tasktracker.entity.Project;
import com.example.tasktracker.exception.ResourceNotFoundException;
import com.example.tasktracker.repository.ProjectRepository;
import com.example.tasktracker.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository repository;

    @InjectMocks
    private ProjectServiceImpl service;

    /**
     * Verifies successful project retrieval by ID.
     */
    @Test
    void shouldGetProjectById() {

        Project project = new Project();
        project.setId(1L);
        project.setName("Task Tracker");

        when(repository.findById(1L))
                .thenReturn(Optional.of(project));

        ProjectResponse result = service.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Task Tracker", result.getName());

        verify(repository, times(1))
                .findById(1L);
    }

    /**
     * Verifies exception is thrown when project is missing.
     */
    @Test
    void shouldThrowWhenProjectNotFound() {

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.getById(1L)
        );

        verify(repository, times(1))
                .findById(1L);
    }

    /**
     * Verifies project deletion behavior.
     */
    @Test
    void shouldDeleteProject() {

        Project project = new Project();
        project.setId(1L);

        when(repository.findById(1L))
                .thenReturn(Optional.of(project));

        service.delete(1L);

        verify(repository, times(1))
                .delete(project);
    }

    /**
     * Verifies retrieval of all projects.
     */
    @Test
    void shouldReturnAllProjects() {

        Project p1 = new Project();
        p1.setId(1L);
        p1.setName("Project A");

        Project p2 = new Project();
        p2.setId(2L);
        p2.setName("Project B");

        when(repository.findAll())
                .thenReturn(Arrays.asList(p1, p2));

        List<ProjectResponse> result = service.getAll();

        assertEquals(2, result.size());
        assertEquals("Project A", result.get(0).getName());
        assertEquals("Project B", result.get(1).getName());

        verify(repository, times(1))
                .findAll();
    }

    /**
     * Verifies empty list is returned when database has no projects.
     */
    @Test
    void shouldReturnEmptyListWhenNoProjectsExist() {

        when(repository.findAll())
                .thenReturn(Collections.emptyList());

        List<ProjectResponse> result = service.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repository, times(1))
                .findAll();
    }

    /**
     * Verifies service handles null description correctly.
     */
    @Test
    void shouldHandleProjectWithNullDescription() {

        Project project = new Project();
        project.setId(1L);
        project.setName("Internal Tool");
        project.setDescription(null);

        when(repository.findById(1L))
                .thenReturn(Optional.of(project));

        ProjectResponse response = service.getById(1L);

        assertNotNull(response);
        assertNull(response.getDescription());

        verify(repository)
                .findById(1L);
    }

    /**
     * Verifies repository method is called only once.
     */
    @Test
    void shouldCallRepositoryOnlyOnceForGetById() {

        Project project = new Project();
        project.setId(10L);

        when(repository.findById(10L))
                .thenReturn(Optional.of(project));

        service.getById(10L);

        verify(repository, only())
                .findById(10L);
    }

    /**
     * Verifies delete throws exception when project is absent.
     */
    @Test
    void shouldThrowWhenDeletingNonExistingProject() {

        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.delete(999L)
        );

        verify(repository, never())
                .delete(any(Project.class));
    }
}