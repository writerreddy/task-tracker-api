package com.example.tasktracker.controller;

import com.example.tasktracker.config.TaskProperties;
import com.example.tasktracker.dto.request.UserRequest;
import com.example.tasktracker.dto.response.UserResponse;
import com.example.tasktracker.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final TaskProperties taskProperties;

    public UserController(UserService userService, TaskProperties taskProperties) {
        this.userService = userService;
        this.taskProperties = taskProperties;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody UserRequest request) {
        return userService.create(request);
    }

    @GetMapping
    public Object getAll(@RequestParam(required = false) Integer page,
                         @RequestParam(required = false) Integer size,
                         @RequestParam(defaultValue = "id") String sortBy,
                         @RequestParam(defaultValue = "asc") String sortDir) {
        if (page == null && size == null) {
            return userService.getAll();
        }
        int resolvedPage = page == null ? 0 : page.intValue();
        int resolvedSize = size == null ? taskProperties.getDefaultPageSize() : size.intValue();
        return userService.getAll(resolvedPage, resolvedSize, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        return userService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
