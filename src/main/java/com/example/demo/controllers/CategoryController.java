package com.example.demo.controllers;

import com.example.demo.dto.CategoryCreateRequest;
import com.example.demo.dto.CategoryCreateResponse;
import com.example.demo.dto.CategoryDto;
import com.example.demo.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "News", description = "API для управления категорями")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @Operation(summary = "Создание")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryCreateResponse createCategory(@RequestBody @Valid CategoryCreateRequest categoryCreateRequest) {
        return categoryService.createCategory(categoryCreateRequest);
    }

    @PutMapping
    @Operation(summary = "Обновление")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto updateCategory(@RequestBody CategoryDto dto) {
        return categoryService.updateCategory(dto);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Получение категории по id")
    public ResponseEntity<CategoryDto> deleteCategory(@PathVariable("id") int id) {
        final CategoryDto deletedCategory = categoryService.deleteCategory(id);
        return new ResponseEntity<>(deletedCategory, HttpStatus.CREATED);
    }
}
