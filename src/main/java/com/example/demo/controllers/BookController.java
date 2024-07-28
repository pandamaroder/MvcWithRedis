package com.example.demo.controllers;

import com.example.demo.dto.BookCreateRequest;
import com.example.demo.dto.BookDto;
import com.example.demo.services.BooksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/Books")
@Tag(name = "Books", description = "API для управления каталогом книг")
@RequiredArgsConstructor
public class BookController {

    private final BooksService booksService;


    @GetMapping("/byTitle")
    @Operation(summary = "Найти", description = "Найти по названию содержащему")
    public Collection<BookDto> findBooks(@RequestParam(name = "title", defaultValue = "") String name) {

        return booksService.findByTitle(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать новую книгу ", description = "Создание новой книги")
    public BookDto create(@RequestBody BookCreateRequest bookCreateRequest) {

        return booksService.createBooks(bookCreateRequest);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBooks(@PathVariable("id") long id) {
        booksService.deleteBooks(id);
    }

    @PutMapping
    @Operation(summary = "Обновление")
    @ResponseStatus(HttpStatus.OK)
    public BookDto updateBooks(@RequestBody BookDto dto) {
        return booksService.updateBooks(dto);
    }
}
