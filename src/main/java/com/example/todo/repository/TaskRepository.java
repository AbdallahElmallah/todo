package com.example.todo.repository;

import com.example.todo.model.Task;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TaskRepository extends JpaRepository<Task,UUID>{

    @Query("SELECT t FROM Task t WHERE t.title like %:text%  OR t.description like %:text%")
    public Page<Task> findByTitleOrDescriptionLike(@Param("text") String text , Pageable pageable);
}