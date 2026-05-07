package com.example.tasktracker.service;

import com.example.tasktracker.config.TaskProperties;
import com.example.tasktracker.dto.request.TaskRequest;
import com.example.tasktracker.dto.response.TaskResponse;
import com.example.tasktracker.entity.AppUser;
import com.example.tasktracker.entity.Project;
import com.example.tasktracker.entity.Task;
import com.example.tasktracker.enums.Priority;
import com.example.tasktracker.enums.TaskStatus;
import com.example.tasktracker.enums.UserRole;
import com.example.tasktracker.exception.ResourceNotFoundException;
import com.example.tasktracker.repository.ProjectRepository;
import com.example.tasktracker.repository.TaskRepository;
import com.example.tasktracker.repository.UserRepository;
import com.example.tasktracker.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskProperties taskProperties;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;
    private Project project;
    private AppUser user;
    private TaskRequest request;

    /**
     * Initializes reusable test data before each test.
     */
    @BeforeEach
    void setUp() {

        project = new Project();
        project.setId(1L);
        project.setName("Project A");

        user = new AppUser();
        user.setId(2L);
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setRole(UserRole.DEVELOPER);

        task = new Task();
        task.setId(10L);
        task.setTitle("Implement login");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setPriority(Priority.HIGH);
        task.setProject(project);
        task.setAssignee(user);
        task.setDueDate(LocalDate.of(2026, 5, 20));

        request = new TaskRequest();
        request.setTitle("Implement login");
        request.setStatus(TaskStatus.IN_PROGRESS);
        request.setPriority(Priority.HIGH);
        request.setProjectId(1L);
        request.setAssigneeId(2L);
        request.setDueDate(LocalDate.of(2026, 5, 20));
    }

    /**
     * Verifies that a task is successfully created
     * when all required data is valid.
     */
    @Test
    void shouldCreateTask() {

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(user));

        when(taskRepository.save(any(Task.class)))
                .thenReturn(task);

        TaskResponse response = taskService.create(request);

        assertNotNull(response);
        assertEquals("Implement login", response.getTitle());
        assertEquals("HIGH", response.getPriority());

        verify(taskRepository).save(any(Task.class));
    }

    /**
     * Verifies that the default priority from configuration
     * is applied when priority is not supplied.
     */
    @Test
    void shouldUseDefaultPriorityWhenMissing() {

        request.setPriority(null);

        when(taskProperties.getDefaultPriority())
                .thenReturn("MEDIUM");

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(user));

        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> {
                    Task saved = invocation.getArgument(0);
                    saved.setId(11L);
                    return saved;
                });

        TaskResponse response = taskService.create(request);

        assertEquals("MEDIUM", response.getPriority());
    }

    /**
     * Verifies that tasks can be filtered by status.
     */
    @Test
    void shouldReturnFilteredTasksByStatus() {

        when(taskRepository.findByStatus(TaskStatus.IN_PROGRESS))
                .thenReturn(Arrays.asList(task));

        assertEquals(
                1,
                taskService.getAll(TaskStatus.IN_PROGRESS, null).size()
        );

        verify(taskRepository).findByStatus(TaskStatus.IN_PROGRESS);
    }

    /**
     * Verifies that an empty list is returned
     * when no tasks match the filter.
     */
    @Test
    void shouldReturnEmptyListWhenNoTasksExist() {

        when(taskRepository.findByStatus(TaskStatus.DONE))
                .thenReturn(Collections.emptyList());

        assertTrue(
                taskService.getAll(TaskStatus.DONE, null).isEmpty()
        );
    }

    /**
     * Verifies that a task can be fetched successfully by ID.
     */
    @Test
    void shouldReturnTaskById() {

        when(taskRepository.findById(10L))
                .thenReturn(Optional.of(task));

        TaskResponse response = taskService.getById(10L);

        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("Implement login", response.getTitle());

        verify(taskRepository).findById(10L);
    }

    /**
     * Verifies that ResourceNotFoundException is thrown
     * when a task does not exist.
     */
    @Test
    void shouldThrowWhenTaskNotFound() {

        when(taskRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.getById(99L)
        );

        verify(taskRepository).findById(99L);
    }

    /**
     * Verifies that a task can be updated successfully.
     */
    @Test
    void shouldUpdateTask() {

        when(taskRepository.findById(10L))
                .thenReturn(Optional.of(task));

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(user));

        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponse response = taskService.update(10L, request);

        assertEquals("IN_PROGRESS", response.getStatus());
        assertEquals("HIGH", response.getPriority());

        verify(taskRepository).save(any(Task.class));
    }

    /**
     * Verifies that updating a non-existing task
     * throws ResourceNotFoundException.
     */
    @Test
    void shouldThrowWhenUpdatingNonExistingTask() {

        when(taskRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.update(999L, request)
        );

        verify(taskRepository, never()).save(any(Task.class));
    }

    /**
     * Verifies that a task can be deleted successfully.
     */
    @Test
    void shouldDeleteTask() {

        when(taskRepository.findById(10L))
                .thenReturn(Optional.of(task));

        taskService.delete(10L);

        verify(taskRepository).delete(task);
    }

    /**
     * Verifies that deleting a non-existing task
     * throws ResourceNotFoundException.
     */
    @Test
    void shouldThrowWhenDeletingTaskThatDoesNotExist() {

        when(taskRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.delete(999L)
        );

        verify(taskRepository, never()).delete(any(Task.class));
    }

    /**
     * Verifies that task creation fails
     * when the project does not exist.
     */
    @Test
    void shouldThrowWhenProjectNotFoundDuringCreate() {

        request.setProjectId(100L);

        when(projectRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.create(request)
        );

        verify(taskRepository, never()).save(any(Task.class));
    }

    /**
     * Verifies that task creation fails
     * when the assignee does not exist.
     */
    @Test
    void shouldThrowWhenAssigneeNotFoundDuringCreate() {

        request.setAssigneeId(999L);

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.create(request)
        );

        verify(taskRepository, never()).save(any(Task.class));
    }

    /**
     * Verifies that tasks can be retrieved by project ID.
     */
    @Test
    void shouldReturnTasksByProjectId() {

        when(taskRepository.findByProjectId(1L))
                .thenReturn(Arrays.asList(task));

        assertEquals(
                1,
                taskService.getAll(null, 1L).size()
        );

        verify(taskRepository).findByProjectId(1L);
    }

    /**
     * Verifies that nullable due dates are handled safely.
     */
    @Test
    void shouldHandleNullDueDate() {

        task.setDueDate(null);

        when(taskRepository.findById(10L))
                .thenReturn(Optional.of(task));

        TaskResponse response = taskService.getById(10L);

        assertNotNull(response);
        assertNull(response.getDueDate());
    }

    /**
     * Verifies that repository methods are not called unnecessarily.
     */
    @Test
    void shouldCallRepositoryOnlyOnceForGetById() {

        when(taskRepository.findById(10L))
                .thenReturn(Optional.of(task));

        taskService.getById(10L);

        verify(taskRepository, only()).findById(10L);
    }
}