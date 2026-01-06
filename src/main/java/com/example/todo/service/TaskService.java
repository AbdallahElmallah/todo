package com.example.todo.service;

import com.example.todo.dto.TaskRequestDTO;
import com.example.todo.exception.TaskNotFoundException;
import com.example.todo.model.Task;
import com.example.todo.repository.TaskRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service 
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Page<Task> getPaginatedTasks(int page, int size){
        PageRequest pageable = PageRequest.of(page, size);
        return taskRepository.findAll(pageable);
    }

    public List<Task> findTasks(String text){
        return taskRepository.findByTitleOrDescription(text);
        
    }
    
    public Task create(TaskRequestDTO request) {
        Task newTask = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .completed(false)
                .createdAt(LocalDateTime.now())
                .build();

        return taskRepository.save(newTask);
    }

    public Task getById(UUID id) {
        return getTaskOrThrow(id);
    }

    public Task update(UUID id, TaskRequestDTO request) {
        Task existingTask = getTaskOrThrow(id);

        existingTask.setTitle(request.getTitle());
        existingTask.setDescription(request.getDescription());
        existingTask.setCompleted(request.isCompleted());

        return taskRepository.save(existingTask);
    }

    public void delete(UUID id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    private Task getTaskOrThrow(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
    }


   

}