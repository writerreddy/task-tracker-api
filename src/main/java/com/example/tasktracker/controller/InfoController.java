package com.example.tasktracker.controller;

import com.example.tasktracker.config.AppConfigProperties;
import com.example.tasktracker.dto.response.InfoResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for providing application information.
 *
 * Exposes application metadata such as name and version.
 */
@RestController
@RequestMapping("/api/info")
public class InfoController {

    private final AppConfigProperties appConfigProperties;

    /**
     * Constructs an InfoController with application configuration properties.
     *
     * @param appConfigProperties application configuration values
     */
    public InfoController(AppConfigProperties appConfigProperties) {
        this.appConfigProperties = appConfigProperties;
    }

    /**
     * Retrieves application information.
     *
     * @return application name and version details
     */
    @GetMapping
    public InfoResponse getInfo() {
        return new InfoResponse(appConfigProperties.getName(), appConfigProperties.getVersion());
    }
}
