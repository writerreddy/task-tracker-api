package com.example.tasktracker.service.impl;

import com.example.tasktracker.dto.request.ProjectRequest;
import com.example.tasktracker.dto.response.PagedResponse;
import com.example.tasktracker.dto.response.ProjectResponse;
import com.example.tasktracker.dto.response.TaskResponse;
import com.example.tasktracker.entity.Project;
import com.example.tasktracker.exception.DuplicateResourceException;
import com.example.tasktracker.exception.ResourceNotFoundException;
import com.example.tasktracker.repository.ProjectRepository;
import com.example.tasktracker.repository.TaskRepository;
import com.example.tasktracker.service.ProjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service implementation for managing project operations.
 *
 * Handles project creation, retrieval, update,
 * deletion, and associated task retrieval.
 */
@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    /**
     * Constructs a ProjectServiceImpl with required repositories.
     *
     * @param projectRepository repository for project persistence
     * @param taskRepository repository for task persistence
     */
    public ProjectServiceImpl(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    /**
     * Creates a new project.
     *
     * @param request project creation request
     * @return created project response
     */
    @Override
    public ProjectResponse create(ProjectRequest request) {
        if (projectRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Project with name '" + request.getName() + "' already exists");
        }
        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        return mapToResponse(projectRepository.save(project));
    }

    /**
     * Retrieves all projects.
     *
     * @return list of project responses
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponse> getAll() {
        List<ProjectResponse> responses = new ArrayList<ProjectResponse>();
        for (Project project : projectRepository.findAll()) {
            responses.add(mapToResponse(project));
        }
        return responses;
    }

    /**
     * Retrieves paginated project data.
     *
     * @param page page number
     * @param size page size
     * @param sortBy field to sort by
     * @param sortDir sorting direction
     * @return paginated project response
     */
    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ProjectResponse> getAll(int page, int size, String sortBy, String sortDir) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Page<Project> result = projectRepository.findAll(PageRequest.of(page, size, sort));
        List<ProjectResponse> content = new ArrayList<ProjectResponse>();
        for (Project project : result.getContent()) {
            content.add(mapToResponse(project));
        }
        return buildPage(content, result);
    }

    /**
     * Retrieves a project by ID.
     *
     * @param id project identifier
     * @return project response
     */
    @Override
    @Transactional(readOnly = true)
    public ProjectResponse getById(Long id) {
        return mapToResponse(findProject(id));
    }

    /**
     * Updates an existing project.
     *
     * @param id project identifier
     * @param request updated project details
     * @return updated project response
     */
    @Override
    public ProjectResponse update(Long id, ProjectRequest request) {
        Project project = findProject(id);
        if (!project.getName().equals(request.getName()) && projectRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Project with name '" + request.getName() + "' already exists");
        }
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        return mapToResponse(projectRepository.save(project));
    }

    /**
     * Deletes a project by ID.
     *
     * @param id project identifier
     */
    @Override
    public void delete(Long id) {
        Project project = findProject(id);
        projectRepository.delete(project);
    }

    /**
     * Retrieves all tasks associated with a project.
     *
     * @param projectId project identifier
     * @return list of task responses
     */
    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByProjectId(Long projectId) {
        findProject(projectId);
        List<TaskResponse> responses = new ArrayList<TaskResponse>();
        for (com.example.tasktracker.entity.Task task : taskRepository.findByProjectId(projectId)) {
            responses.add(TaskMapper.toResponse(task));
        }
        return responses;
    }

    /**
     * Finds a project by ID.
     *
     * @param id project identifier
     * @return project entity
     */
    private Project findProject(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Project not found with id: " + id));
    }

    /**
     * Maps project entity to response DTO.
     *
     * @param project project entity
     * @return project response
     */
    private ProjectResponse mapToResponse(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId());
        response.setName(project.getName());
        response.setDescription(project.getDescription());
        response.setCreatedAt(project.getCreatedAt());
        return response;
    }

    /**
     * Builds paginated project response.
     *
     * @param content project response content
     * @param page page metadata
     * @return paged response
     */
    private PagedResponse<ProjectResponse> buildPage(List<ProjectResponse> content, Page<Project> page) {
        PagedResponse<ProjectResponse> response = new PagedResponse<ProjectResponse>();
        response.setContent(content);
        response.setPage(page.getNumber());
        response.setSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setFirst(page.isFirst());
        response.setLast(page.isLast());
        return response;
    }
}