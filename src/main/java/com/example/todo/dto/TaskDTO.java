package com.example.todo.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class TaskDTO {
    private UUID id;
    private String title;
    private String description;
    private boolean completed;
    private LocalDateTime createdAt;

}