package com.example.tasktracker.service;

import com.example.tasktracker.dto.request.ProjectRequest;
import com.example.tasktracker.dto.response.PagedResponse;
import com.example.tasktracker.dto.response.ProjectResponse;

import java.util.List;

public interface ProjectService {
    ProjectResponse create(ProjectRequest request);
    List<ProjectResponse> getAll();
    PagedResponse<ProjectResponse> getAll(int page, int size, String sortBy, String sortDir);
    ProjectResponse getById(Long id);
    ProjectResponse update(Long id, ProjectRequest request);
    void delete(Long id);
    List<com.example.tasktracker.dto.response.TaskResponse> getTasksByProjectId(Long projectId);
}
