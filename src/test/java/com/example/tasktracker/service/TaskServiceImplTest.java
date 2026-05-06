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

    @Test
    void shouldCreateTask() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponse response = taskService.create(request);

        assertEquals("Implement login", response.getTitle());
        assertEquals("HIGH", response.getPriority());
    }

    @Test
    void shouldUseDefaultPriorityWhenMissing() {
        request.setPriority(null);
        when(taskProperties.getDefaultPriority()).thenReturn("MEDIUM");
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task saved = invocation.getArgument(0);
            saved.setId(11L);
            return saved;
        });

        TaskResponse response = taskService.create(request);

        assertEquals("MEDIUM", response.getPriority());
    }

    @Test
    void shouldReturnFilteredTasksByStatus() {
        when(taskRepository.findByStatus(TaskStatus.IN_PROGRESS)).thenReturn(Arrays.asList(task));

        assertEquals(1, taskService.getAll(TaskStatus.IN_PROGRESS, null).size());
    }

    @Test
    void shouldThrowWhenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, new org.junit.jupiter.api.function.Executable() {
            @Override
            public void execute() {
                taskService.getById(99L);
            }
        });
    }

    @Test
    void shouldUpdateTask() {
        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponse response = taskService.update(10L, request);

        assertEquals("IN_PROGRESS", response.getStatus());
    }

    @Test
    void shouldDeleteTask() {
        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));

        taskService.delete(10L);

        verify(taskRepository).delete(task);
    }

    @Test
    void shouldThrowWhenProjectNotFoundDuringCreate() {
        when(projectRepository.findById(100L)).thenReturn(Optional.empty());
        request.setProjectId(100L);

        assertThrows(ResourceNotFoundException.class, new org.junit.jupiter.api.function.Executable() {
            @Override
            public void execute() {
                taskService.create(request);
            }
        });
    }
}
