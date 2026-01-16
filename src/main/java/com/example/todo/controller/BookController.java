package com.example.todo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.dto.BookResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Operation(summary = "Get Book Details", description = "Returns book data as a Map while using a DTO for Swagger documentation")
    @ApiResponse(
        responseCode = "200",
        description = "Successful retrieval of book details",
        content = @Content(
            schema = @Schema(implementation = BookResponseDTO.class)
        )
    )
    @GetMapping("/{id}")
    public Map<String, Object> getBookById(@PathVariable String id) {
       
        Map<String, Object> bookData = new HashMap<>();
       
        bookData.put("bookTitle", "java");
        bookData.put("authorName", "author");
        bookData.put("price", 50.00);
        
        return bookData; 
    }
}