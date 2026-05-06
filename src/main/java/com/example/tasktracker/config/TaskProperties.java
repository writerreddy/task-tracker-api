package com.example.tasktracker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TaskProperties {

    @Value("${task.default-priority}")
    private String defaultPriority;

    @Value("${pagination.default-page-size}")
    private int defaultPageSize;

    public String getDefaultPriority() {
        return defaultPriority;
    }

    public int getDefaultPageSize() {
        return defaultPageSize;
    }
}
