package com.example.tasktracker.service.impl;

import com.example.tasktracker.dto.response.TaskResponse;
import com.example.tasktracker.entity.Task;

public final class TaskMapper {

    private TaskMapper() {
    }

    public static TaskResponse toResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setStatus(task.getStatus().name());
        response.setPriority(task.getPriority().name());
        response.setProjectId(task.getProject().getId());
        response.setProjectName(task.getProject().getName());
        if (task.getAssignee() != null) {
            response.setAssigneeId(task.getAssignee().getId());
            response.setAssigneeName(task.getAssignee().getName());
        }
        response.setDueDate(task.getDueDate());
        return response;
    }
}
