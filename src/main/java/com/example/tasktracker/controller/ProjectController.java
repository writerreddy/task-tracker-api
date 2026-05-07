package com.example.tasktracker.controller;

import com.example.tasktracker.config.TaskProperties;
import com.example.tasktracker.dto.request.ProjectRequest;
import com.example.tasktracker.dto.response.ProjectResponse;
import com.example.tasktracker.dto.response.TaskResponse;
import com.example.tasktracker.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * REST controller for managing project-related operations.
 *
 * Provides endpoints for creating, retrieving, updating,
 * deleting projects, and fetching tasks associated with a project.
 */
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final TaskProperties taskProperties;

    /**
     * Constructs a ProjectController with required dependencies.
     *
     * @param projectService service for project operations
     * @param taskProperties pagination configuration properties
     */
    public ProjectController(ProjectService projectService, TaskProperties taskProperties) {
        this.projectService = projectService;
        this.taskProperties = taskProperties;
    }

    /**
     * Creates a new project.
     *
     * @param request project creation request
     * @return created project details
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse create(@Valid @RequestBody ProjectRequest request) {
        return projectService.create(request);
    }

    /**
     * Retrieves all projects with optional pagination and sorting.
     *
     * @param page page number
     * @param size page size
     * @param sortBy field to sort by
     * @param sortDir sorting direction
     * @return list or paginated project response
     */
    @GetMapping
    public Object getAll(@RequestParam(required = false) Integer page,
                         @RequestParam(required = false) Integer size,
                         @RequestParam(defaultValue = "id") String sortBy,
                         @RequestParam(defaultValue = "asc") String sortDir) {
        if (page == null && size == null) {
            return projectService.getAll();
        }
        int resolvedPage = page == null ? 0 : page.intValue();
        int resolvedSize = size == null ? taskProperties.getDefaultPageSize() : size.intValue();
        return projectService.getAll(resolvedPage, resolvedSize, sortBy, sortDir);
    }

    /**
     * Retrieves a project by its ID.
     *
     * @param id project identifier
     * @return project details
     */
    @GetMapping("/{id}")
    public ProjectResponse getById(@PathVariable Long id) {
        return projectService.getById(id);
    }

    /**
     * Updates an existing project.
     *
     * @param id project identifier
     * @param request updated project details
     * @return updated project response
     */
    @PutMapping("/{id}")
    public ProjectResponse update(@PathVariable Long id, @Valid @RequestBody ProjectRequest request) {
        return projectService.update(id, request);
    }

    /**
     * Deletes a project by its ID.
     *
     * @param id project identifier
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        projectService.delete(id);
    }

    /**
     * Retrieves all tasks associated with a project.
     *
     * @param id project identifier
     * @return list of project tasks
     */
    @GetMapping("/{id}/tasks")
    public List<TaskResponse> getTasksByProject(@PathVariable Long id) {
        return projectService.getTasksByProjectId(id);
    }
}