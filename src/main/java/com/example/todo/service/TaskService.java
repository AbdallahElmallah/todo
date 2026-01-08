package com.example.todo.service;

import com.example.todo.dto.TaskDTO;
import com.example.todo.dto.TaskRequestDTO;
import com.example.todo.exception.TaskNotFoundException;
import com.example.todo.model.Task;
import com.example.todo.repository.TaskRepository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Page<TaskDTO> getPaginatedTasks(int page, int size){
        PageRequest pageable = PageRequest.of(page, size);
        return taskRepository.findAll(pageable).map(this::convertTaskToDTO);
    }

    public Page<TaskDTO> findTasks(String text,int page, int size){
        PageRequest pageable = PageRequest.of(page, size);

        return taskRepository.findByTitleOrDescriptionLike(text, pageable).map(this::convertTaskToDTO);
        
    }
   
    @Transactional
    public TaskDTO create(TaskRequestDTO request) {
        Task newTask = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .completed(false)
                .createdAt(LocalDateTime.now())
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

        return convertTaskToDTO( taskRepository.save(existingTask));
    }

    @Transactional
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

 private TaskDTO convertTaskToDTO(Task task) {
    return TaskDTO.builder()
            .id(task.getId())
            .title(task.getTitle())
            .description(task.getDescription())
            .completed(task.isCompleted())
            .build();
}
   

}