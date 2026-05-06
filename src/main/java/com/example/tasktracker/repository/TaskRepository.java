package com.example.tasktracker.repository;

import com.example.tasktracker.entity.Task;
import com.example.tasktracker.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByProjectIdAndStatus(Long projectId, TaskStatus status);

    Page<Task> findAll(Pageable pageable);
    Page<Task> findByStatus(TaskStatus status, Pageable pageable);
    Page<Task> findByProjectId(Long projectId, Pageable pageable);
    Page<Task> findByProjectIdAndStatus(Long projectId, TaskStatus status, Pageable pageable);
}
