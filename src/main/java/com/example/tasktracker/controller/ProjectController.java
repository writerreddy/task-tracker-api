package com.example.tasktracker.controller;

import com.example.tasktracker.config.TaskProperties;
import com.example.tasktracker.dto.request.ProjectRequest;
import com.example.tasktracker.dto.response.PagedResponse;
import com.example.tasktracker.dto.response.ProjectResponse;
import com.example.tasktracker.dto.response.TaskResponse;
import com.example.tasktracker.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final TaskProperties taskProperties;

    public ProjectController(ProjectService projectService, TaskProperties taskProperties) {
        this.projectService = projectService;
        this.taskProperties = taskProperties;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse create(@Valid @RequestBody ProjectRequest request) {
        return projectService.create(request);
    }

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

    @GetMapping("/{id}")
    public ProjectResponse getById(@PathVariable Long id) {
        return projectService.getById(id);
    }

    @PutMapping("/{id}")
    public ProjectResponse update(@PathVariable Long id, @Valid @RequestBody ProjectRequest request) {
        return projectService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        projectService.delete(id);
    }

    @GetMapping("/{id}/tasks")
    public List<TaskResponse> getTasksByProject(@PathVariable Long id) {
        return projectService.getTasksByProjectId(id);
    }
}
