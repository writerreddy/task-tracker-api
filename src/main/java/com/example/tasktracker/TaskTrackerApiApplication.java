package com.example.tasktracker;

import com.example.tasktracker.config.AppConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppConfigProperties.class)
public class TaskTrackerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskTrackerApiApplication.class, args);
    }
}
