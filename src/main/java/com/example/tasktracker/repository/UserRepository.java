package com.example.tasktracker.repository;

import com.example.tasktracker.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    boolean existsByEmail(String email);
}
