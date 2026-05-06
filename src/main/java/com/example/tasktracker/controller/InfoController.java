package com.example.tasktracker.controller;

import com.example.tasktracker.config.AppConfigProperties;
import com.example.tasktracker.dto.response.InfoResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/info")
public class InfoController {

    private final AppConfigProperties appConfigProperties;

    public InfoController(AppConfigProperties appConfigProperties) {
        this.appConfigProperties = appConfigProperties;
    }

    @GetMapping
    public InfoResponse getInfo() {
        return new InfoResponse(appConfigProperties.getName(), appConfigProperties.getVersion());
    }
}
