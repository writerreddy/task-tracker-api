package com.example.tasktracker.service;

import com.example.tasktracker.dto.request.UserRequest;
import com.example.tasktracker.dto.response.PagedResponse;
import com.example.tasktracker.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse create(UserRequest request);
    List<UserResponse> getAll();
    PagedResponse<UserResponse> getAll(int page, int size, String sortBy, String sortDir);
    UserResponse getById(Long id);
    UserResponse update(Long id, UserRequest request);
    void delete(Long id);
}
