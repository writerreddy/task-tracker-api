package com.example.tasktracker.controller;

import com.example.tasktracker.config.TaskProperties;
import com.example.tasktracker.dto.request.TaskRequest;
import com.example.tasktracker.dto.response.TaskResponse;
import com.example.tasktracker.enums.TaskStatus;
import com.example.tasktracker.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * REST controller for managing task-related operations.
 *
 * Provides endpoints for creating, retrieving,
 * updating, and deleting tasks.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskProperties taskProperties;

    /**
     * Constructs a TaskController with required dependencies.
     *
     * @param taskService service for task operations
     * @param taskProperties pagination configuration properties
     */
    public TaskController(TaskService taskService, TaskProperties taskProperties) {
        this.taskService = taskService;
        this.taskProperties = taskProperties;
    }

    /**
     * Creates a new task.
     *
     * @param request task creation request
     * @return created task details
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse create(@Valid @RequestBody TaskRequest request) {
        return taskService.create(request);
    }

    /**
     * Retrieves tasks with optional filtering, pagination, and sorting.
     *
     * @param status task status filter
     * @param projectId project identifier filter
     * @param page page number
     * @param size page size
     * @param sortBy field to sort by
     * @param sortDir sorting direction
     * @return list or paginated task response
     */
    @GetMapping
    public Object getAll(@RequestParam(required = false) TaskStatus status,
                         @RequestParam(required = false) Long projectId,
                         @RequestParam(required = false) Integer page,
                         @RequestParam(required = false) Integer size,
                         @RequestParam(defaultValue = "id") String sortBy,
                         @RequestParam(defaultValue = "asc") String sortDir) {
        if (page == null && size == null) {
            return taskService.getAll(status, projectId);
        }
        int resolvedPage = page == null ? 0 : page.intValue();
        int resolvedSize = size == null ? taskProperties.getDefaultPageSize() : size.intValue();
        return taskService.getAll(status, projectId, resolvedPage, resolvedSize, sortBy, sortDir);
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id task identifier
     * @return task details
     */
    @GetMapping("/{id}")
    public TaskResponse getById(@PathVariable Long id) {
        return taskService.getById(id);
    }

    /**
     * Updates an existing task.
     *
     * @param id task identifier
     * @param request updated task details
     * @return updated task response
     */
    @PutMapping("/{id}")
    public TaskResponse update(@PathVariable Long id, @Valid @RequestBody TaskRequest request) {
        return taskService.update(id, request);
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id task identifier
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }
}