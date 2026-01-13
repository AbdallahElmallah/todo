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

    @Query("SELECT t FROM Task t WHERE (t.title LIKE %:text% OR t.description LIKE %:text%) AND (t.userId = :userId) AND (t.realmId = :realmId) ")
    public Page<Task> findByTitleOrDescriptionLikeAndUserIdAndRealmId(@Param("text") String text ,@Param("userId") String user,@Param("realmId") String realmId, Pageable pageable);

    Page<Task> findByUserIdAndRealmId(String user, String realmId,Pageable pageable);
}