package com.example.tasktracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling home endpoint requests.
 *
 * Provides a basic welcome message and API usage information.
 */
@RestController
public class HomeController {

    /**
     * Returns the application status message.
     *
     * @return a welcome message with API endpoint details
     */
    @GetMapping("/")
    public String home() {
        return "Task Tracker API is running. Use /swagger-ui.html or /api/tasks";
    }
}