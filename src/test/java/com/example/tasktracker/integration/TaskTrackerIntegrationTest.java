package com.example.tasktracker.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for validating
 * end-to-end API functionality.
 *
 * Tests application info retrieval,
 * project operations, and validation handling.
 */
@SpringBootTest
@AutoConfigureMockMvc
class TaskTrackerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Verifies application information endpoint.
     *
     * @throws Exception if request execution fails
     */
    @Test
    void shouldReturnApplicationInfo() throws Exception {
        mockMvc.perform(get("/api/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appName").value("Task Tracker API"))
                .andExpect(jsonPath("$.appVersion").value("1.0.0"));
    }

    /**
     * Verifies project creation
     * and project listing endpoints.
     *
     * @throws Exception if request execution fails
     */
    @Test
    void shouldCreateProjectAndListProjects() throws Exception {
        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Project X\",\"description\":\"Integration test project\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Project X"));

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk());
    }

    /**
     * Verifies validation error handling
     * for invalid user creation requests.
     *
     * @throws Exception if request execution fails
     */
    @Test
    void shouldReturnValidationErrorForInvalidUser() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"email\":\"not-an-email\",\"role\":null}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }
}