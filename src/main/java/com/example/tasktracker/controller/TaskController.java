package com.example.tasktracker.controller;

import com.example.tasktracker.config.TaskProperties;
import com.example.tasktracker.dto.request.TaskRequest;
import com.example.tasktracker.dto.response.TaskResponse;
import com.example.tasktracker.enums.TaskStatus;
import com.example.tasktracker.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskProperties taskProperties;

    public TaskController(TaskService taskService, TaskProperties taskProperties) {
        this.taskService = taskService;
        this.taskProperties = taskProperties;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse create(@Valid @RequestBody TaskRequest request) {
        return taskService.create(request);
    }

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

    @GetMapping("/{id}")
    public TaskResponse getById(@PathVariable Long id) {
        return taskService.getById(id);
    }

    @PutMapping("/{id}")
    public TaskResponse update(@PathVariable Long id, @Valid @RequestBody TaskRequest request) {
        return taskService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }
}
