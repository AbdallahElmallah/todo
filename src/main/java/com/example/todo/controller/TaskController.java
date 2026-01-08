package com.example.todo.controller;

import com.example.todo.dto.TaskDTO;
import com.example.todo.dto.TaskRequestDTO;
import com.example.todo.service.TaskService;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;


    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public Page<TaskDTO> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size)
    {
        return taskService.getPaginatedTasks(page,size);
    }

    @GetMapping("/filter")
    public Page<TaskDTO> searchTasks(
            @RequestParam String text,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return taskService.findTasks(text, page, size);
    }    

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable UUID id) {
        return ResponseEntity.ok(taskService.getById(id));
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskRequestDTO request) {
        TaskDTO createdTask = taskService.create(request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);    
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable UUID id, @Valid @RequestBody TaskRequestDTO request) {
        return ResponseEntity.ok(taskService.update(id, request)); 
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build(); 
    }
    
}