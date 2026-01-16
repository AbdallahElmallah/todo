package com.example.todo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class BookResponseDTO {
    @Schema(example = "Java", description = "The title of the book")
    public String bookTitle;

    public String authorName;

    public double price;

    public String isbn;
}