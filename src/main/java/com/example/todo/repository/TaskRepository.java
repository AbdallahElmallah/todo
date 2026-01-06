package com.example.todo.repository;

import com.example.todo.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface TaskRepository {

    Task save(Task task);    

    Optional<Task> findById(UUID id);
    
    List<Task> findAll();
    
    void deleteById(UUID id);
    
    boolean existsById(UUID id);
}