package com.example.tasktracker.service;

import com.example.tasktracker.dto.request.ProjectRequest;
import com.example.tasktracker.dto.response.PagedResponse;
import com.example.tasktracker.dto.response.ProjectResponse;
import com.example.tasktracker.dto.response.TaskResponse;

import java.util.List;

/**
 * Service interface for project operations.
 *
 * Defines methods for managing projects
 * and retrieving associated tasks.
 */
public interface ProjectService {

    /**
     * Creates a new project.
     *
     * @param request project creation request
     * @return created project response
     */
    ProjectResponse create(ProjectRequest request);

    /**
     * Retrieves all projects.
     *
     * @return list of project responses
     */
    List<ProjectResponse> getAll();

    /**
     * Retrieves paginated projects.
     *
     * @param page page number
     * @param size page size
     * @param sortBy field to sort by
     * @param sortDir sorting direction
     * @return paginated project response
     */
    PagedResponse<ProjectResponse> getAll(int page, int size, String sortBy, String sortDir);

    /**
     * Retrieves a project by ID.
     *
     * @param id project identifier
     * @return project response
     */
    ProjectResponse getById(Long id);

    /**
     * Updates an existing project.
     *
     * @param id project identifier
     * @param request updated project details
     * @return updated project response
     */
    ProjectResponse update(Long id, ProjectRequest request);

    /**
     * Deletes a project by ID.
     *
     * @param id project identifier
     */
    void delete(Long id);

    /**
     * Retrieves tasks associated with a project.
     *
     * @param projectId project identifier
     * @return list of task responses
     */
    List<TaskResponse> getTasksByProjectId(Long projectId);
}