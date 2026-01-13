package com.example.todo.service;

import com.example.todo.dto.TaskDTO;
import com.example.todo.dto.TaskRequestDTO;
import com.example.todo.entity.Task;
import com.example.todo.entity.User;
import com.example.todo.exception.TaskNotFoundException;
import com.example.todo.repository.TaskRepository;
import com.example.todo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.oauth2.jwt.Jwt;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private String getCurrentUserSub() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof Jwt jwt) {
            return jwt.getSubject();
        }

        throw new RuntimeException("Authentication principal is not a valid JWT token");
    }
    private String getCurrentRealm() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof Jwt jwt) {
            return jwt.getIssuer().toString();
        }

        throw new RuntimeException("Authentication principal is not a valid JWT token");
    }

    public Page<TaskDTO> getPaginatedTasks(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);

        return taskRepository.findByUserIdAndRealmId(getCurrentUserSub(),getCurrentRealm(), pageable).map(this::convertTaskToDTO);
    }

    public Page<TaskDTO> findTasks(String text, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);

        return taskRepository.findByTitleOrDescriptionLikeAndUserIdAndRealmId(text, getCurrentUserSub(),getCurrentRealm(), pageable)
                .map(this::convertTaskToDTO);
    }

    @Transactional
    public TaskDTO create(TaskRequestDTO request) {

        Task newTask = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .completed(false)
                .createdAt(LocalDateTime.now())
                .userId(getCurrentUserSub())
                .realmId(getCurrentRealm())
                .build();

        Task entityTask = taskRepository.save(newTask);
        return convertTaskToDTO(entityTask);
    }

    public TaskDTO getById(UUID id) {
        return convertTaskToDTO(getTaskOrThrow(id));
    }

    @Transactional
    public TaskDTO update(UUID id, TaskRequestDTO request) {
        Task existingTask = getTaskOrThrow(id);

        existingTask.setTitle(request.getTitle());
        existingTask.setDescription(request.getDescription());
        existingTask.setCompleted(request.isCompleted());

        return convertTaskToDTO(taskRepository.save(existingTask));
    }

    @Transactional
    public void delete(UUID id) {
        Task existingTask = getTaskOrThrow(id);
        taskRepository.delete(existingTask);
    }

    private Task getTaskOrThrow(UUID id) {
        return taskRepository.findById(id)
                .filter(task -> task.getUserId().equals(getCurrentUserSub()))
                .filter(task -> task.getRealmId().equals(getCurrentRealm()))
                .orElseThrow(() -> new TaskNotFoundException("Task not found or access denied"));
    }

    private TaskDTO convertTaskToDTO(Task task) {
        return TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .completed(task.isCompleted())
                .build();
    }
}