package com.example.tasktracker.service;

import com.example.tasktracker.dto.request.TaskRequest;
import com.example.tasktracker.dto.response.PagedResponse;
import com.example.tasktracker.dto.response.TaskResponse;
import com.example.tasktracker.enums.TaskStatus;

import java.util.List;

/**
 * Service interface for task operations.
 *
 * Defines methods for managing tasks
 * and task retrieval operations.
 */
public interface TaskService {

    /**
     * Creates a new task.
     *
     * @param request task creation request
     * @return created task response
     */
    TaskResponse create(TaskRequest request);

    /**
     * Retrieves tasks with optional filters.
     *
     * @param status task status filter
     * @param projectId project identifier filter
     * @return list of task responses
     */
    List<TaskResponse> getAll(TaskStatus status, Long projectId);

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
    PagedResponse<TaskResponse> getAll(TaskStatus status, Long projectId, int page, int size, String sortBy, String sortDir);

    /**
     * Retrieves a task by ID.
     *
     * @param id task identifier
     * @return task response
     */
    TaskResponse getById(Long id);

    /**
     * Updates an existing task.
     *
     * @param id task identifier
     * @param request updated task details
     * @return updated task response
     */
    TaskResponse update(Long id, TaskRequest request);

    /**
     * Deletes a task by ID.
     *
     * @param id task identifier
     */
    void delete(Long id);

    /**
     * Retrieves tasks by project ID.
     *
     * @param projectId project identifier
     * @return list of task responses
     */
    List<TaskResponse> getByProjectId(Long projectId);
}