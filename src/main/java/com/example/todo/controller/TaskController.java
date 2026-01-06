package com.example.todo.controller;

import com.example.todo.dto.TaskRequestDTO;
import com.example.todo.model.Task;
import com.example.todo.service.TaskService;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/tasks")
@Transactional
public class TaskController {

    private final TaskService taskService;


    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public Page<Task> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size)
    {
        return taskService.getPaginatedTasks(page, size);
    }

    @GetMapping("/filter")
    public List<Task> getMethodName(@RequestParam String text ) {
        return taskService.findTasks(text);
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable UUID id) {
        Task task = taskService.getById(id);
        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskRequestDTO request) {
        Task createdTask = taskService.create(request);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED); // status 201 Created
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable UUID id, @Valid @RequestBody TaskRequestDTO request) {
        Task updatedTask = taskService.update(id, request);
        return ResponseEntity.ok(updatedTask); // status 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build(); // status 204 No Content
    }
    
}