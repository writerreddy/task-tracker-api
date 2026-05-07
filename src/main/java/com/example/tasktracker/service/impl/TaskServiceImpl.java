package com.example.tasktracker.service.impl;

import com.example.tasktracker.config.TaskProperties;
import com.example.tasktracker.dto.response.PagedResponse;
import com.example.tasktracker.dto.request.TaskRequest;
import com.example.tasktracker.dto.response.TaskResponse;
import com.example.tasktracker.entity.AppUser;
import com.example.tasktracker.entity.Project;
import com.example.tasktracker.entity.Task;
import com.example.tasktracker.enums.Priority;
import com.example.tasktracker.enums.TaskStatus;
import com.example.tasktracker.exception.ResourceNotFoundException;
import com.example.tasktracker.repository.ProjectRepository;
import com.example.tasktracker.repository.TaskRepository;
import com.example.tasktracker.repository.UserRepository;
import com.example.tasktracker.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service implementation for managing task operations.
 *
 * Handles task creation, retrieval, update,
 * deletion, and filtering operations.
 */
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskProperties taskProperties;

    /**
     * Constructs a TaskServiceImpl with required dependencies.
     *
     * @param taskRepository repository for task persistence
     * @param projectRepository repository for project persistence
     * @param userRepository repository for user persistence
     * @param taskProperties task configuration properties
     */
    public TaskServiceImpl(TaskRepository taskRepository, ProjectRepository projectRepository,
                           UserRepository userRepository, TaskProperties taskProperties) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.taskProperties = taskProperties;
    }

    /**
     * Creates a new task.
     *
     * @param request task creation request
     * @return created task response
     */
    @Override
    public TaskResponse create(TaskRequest request) {
        Project project = findProject(request.getProjectId());
        AppUser assignee = findAssignee(request.getAssigneeId());

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setStatus(request.getStatus());
        task.setPriority(resolvePriority(request.getPriority()));
        task.setProject(project);
        task.setAssignee(assignee);
        task.setDueDate(request.getDueDate());

        return TaskMapper.toResponse(taskRepository.save(task));
    }

    /**
     * Retrieves tasks with optional filters.
     *
     * @param status task status filter
     * @param projectId project identifier filter
     * @return list of task responses
     */
    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getAll(TaskStatus status, Long projectId) {
        List<Task> tasks;
        if (status != null && projectId != null) {
            tasks = taskRepository.findByProjectIdAndStatus(projectId, status);
        } else if (status != null) {
            tasks = taskRepository.findByStatus(status);
        } else if (projectId != null) {
            tasks = taskRepository.findByProjectId(projectId);
        } else {
            tasks = taskRepository.findAll();
        }

        List<TaskResponse> responses = new ArrayList<TaskResponse>();
        for (Task task : tasks) {
            responses.add(TaskMapper.toResponse(task));
        }
        return responses;
    }

    /**
     * Retrieves paginated tasks with optional filters.
     *
     * @param status task status filter
     * @param projectId project identifier filter
     * @param page page number
     * @param size page size
     * @param sortBy field to sort by
     * @param sortDir sorting direction
     * @return paginated task response
     */
    @Override
    @Transactional(readOnly = true)
    public PagedResponse<TaskResponse> getAll(TaskStatus status, Long projectId, int page, int size, String sortBy, String sortDir) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        PageRequest pageable = PageRequest.of(page, size, sort);
        Page<Task> tasks;

        if (status != null && projectId != null) {
            tasks = taskRepository.findByProjectIdAndStatus(projectId, status, pageable);
        } else if (status != null) {
            tasks = taskRepository.findByStatus(status, pageable);
        } else if (projectId != null) {
            tasks = taskRepository.findByProjectId(projectId, pageable);
        } else {
            tasks = taskRepository.findAll(pageable);
        }

        List<TaskResponse> content = new ArrayList<TaskResponse>();
        for (Task task : tasks.getContent()) {
            content.add(TaskMapper.toResponse(task));
        }

        PagedResponse<TaskResponse> response = new PagedResponse<TaskResponse>();
        response.setContent(content);
        response.setPage(tasks.getNumber());
        response.setSize(tasks.getSize());
        response.setTotalElements(tasks.getTotalElements());
        response.setTotalPages(tasks.getTotalPages());
        response.setFirst(tasks.isFirst());
        response.setLast(tasks.isLast());
        return response;
    }

    /**
     * Retrieves a task by ID.
     *
     * @param id task identifier
     * @return task response
     */
    @Override
    @Transactional(readOnly = true)
    public TaskResponse getById(Long id) {
        return TaskMapper.toResponse(findTask(id));
    }

    /**
     * Updates an existing task.
     *
     * @param id task identifier
     * @param request updated task details
     * @return updated task response
     */
    @Override
    public TaskResponse update(Long id, TaskRequest request) {
        Task task = findTask(id);
        task.setTitle(request.getTitle());
        task.setStatus(request.getStatus());
        task.setPriority(resolvePriority(request.getPriority()));
        task.setProject(findProject(request.getProjectId()));
        task.setAssignee(findAssignee(request.getAssigneeId()));
        task.setDueDate(request.getDueDate());
        return TaskMapper.toResponse(taskRepository.save(task));
    }

    /**
     * Deletes a task by ID.
     *
     * @param id task identifier
     */
    @Override
    public void delete(Long id) {
        Task task = findTask(id);
        taskRepository.delete(task);
    }

    /**
     * Retrieves tasks by project ID.
     *
     * @param projectId project identifier
     * @return list of task responses
     */
    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getByProjectId(Long projectId) {
        findProject(projectId);
        List<TaskResponse> responses = new ArrayList<TaskResponse>();
        for (Task task : taskRepository.findByProjectId(projectId)) {
            responses.add(TaskMapper.toResponse(task));
        }
        return responses;
    }

    /**
     * Finds a project by ID.
     *
     * @param id project identifier
     * @return project entity
     */
    private Project findProject(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }

    /**
     * Finds an assignee by ID.
     *
     * @param assigneeId user identifier
     * @return user entity
     */
    private AppUser findAssignee(Long assigneeId) {
        if (assigneeId == null) {
            return null;
        }
        return userRepository.findById(assigneeId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + assigneeId));
    }

    /**
     * Finds a task by ID.
     *
     * @param id task identifier
     * @return task entity
     */
    private Task findTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }

    /**
     * Resolves task priority.
     *
     * @param priority provided priority
     * @return resolved priority value
     */
    private Priority resolvePriority(Priority priority) {
        if (priority != null) {
            return priority;
        }
        return Priority.valueOf(taskProperties.getDefaultPriority());
    }
}