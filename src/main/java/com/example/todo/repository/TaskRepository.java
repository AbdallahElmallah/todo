package com.example.todo.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.todo.entity.Task;
import com.example.todo.entity.User;


public interface TaskRepository extends JpaRepository<Task,UUID>{

    @Query("SELECT t FROM Task t WHERE (t.title LIKE %:text% OR t.description LIKE %:text%) AND t.userId = :userId")
    public Page<Task> findByTitleOrDescriptionLikeAndUserId(@Param("text") String text ,@Param("userId") String user, Pageable pageable);

    Page<Task> findByUserId(String user, Pageable pageable);
}