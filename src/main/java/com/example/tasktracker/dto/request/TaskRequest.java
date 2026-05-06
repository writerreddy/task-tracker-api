package com.example.tasktracker.dto.request;

import com.example.tasktracker.enums.Priority;
import com.example.tasktracker.enums.TaskStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class TaskRequest {

    @NotBlank(message = "Task title is required")
    @Size(max = 150, message = "Task title must not exceed 150 characters")
    private String title;

    @NotNull(message = "Task status is required")
    private TaskStatus status;

    private Priority priority;

    @NotNull(message = "Project id is required")
    private Long projectId;

    private Long assigneeId;

    private LocalDate dueDate;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }
    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public Long getAssigneeId() { return assigneeId; }
    public void setAssigneeId(Long assigneeId) { this.assigneeId = assigneeId; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
}
