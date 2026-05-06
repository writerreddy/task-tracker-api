package com.example.tasktracker.service;

import com.example.tasktracker.dto.request.TaskRequest;
import com.example.tasktracker.dto.response.PagedResponse;
import com.example.tasktracker.dto.response.TaskResponse;
import com.example.tasktracker.entity.Task;
import com.example.tasktracker.enums.TaskStatus;

import java.util.List;

public interface TaskService {
    TaskResponse create(TaskRequest request);
    List<TaskResponse> getAll(TaskStatus status, Long projectId);
    PagedResponse<TaskResponse> getAll(TaskStatus status, Long projectId, int page, int size, String sortBy, String sortDir);
    TaskResponse getById(Long id);
    TaskResponse update(Long id, TaskRequest request);
    void delete(Long id);
    List<TaskResponse> getByProjectId(Long projectId);

}
