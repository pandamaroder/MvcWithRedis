package com.example.demo.services;

import com.example.demo.dto.CategoryCreateRequest;
import com.example.demo.dto.CategoryCreateResponse;
import com.example.demo.dto.CategoryDto;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.model.Category;
import com.example.demo.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService extends BaseService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryCreateResponse createCategory(CategoryCreateRequest request) {
        final Category category = Category.builder()
            .name(cleanData(request.name()))
            .build();
        final Category savedCategory = categoryRepository.save(category);
        return new CategoryCreateResponse(savedCategory.getId(), savedCategory.getName());
    }

    @Transactional(propagation = MANDATORY) // только в рамках существующей категории
    public Category createIfNeedCategory(String categoryName) {
        final String categoryNameCleaned = cleanData(categoryName);
        final Optional<Category> categoryByName = categoryRepository
            .findByName(categoryNameCleaned);
        if (categoryByName.isPresent()) {
            return categoryByName.get();
        }
        final Category category = Category.builder()
            .name(categoryNameCleaned)
            .build();
        return categoryRepository.save(category);
    }

    @Transactional
    public CategoryDto deleteCategory(long categoryId) {
        final Category category = categoryRepository
            .findById(categoryId)
            .orElseThrow(() -> new NotFoundException("Категории с таким ID не существует"));
        categoryRepository.delete(category);
        return new CategoryDto(category.getId(), category.getName());
    }

    @Transactional
    public CategoryDto updateCategory(CategoryDto categoryDto) {

        final Category categoryToUpdate = categoryRepository
            .findById(categoryDto.id())
            .orElseThrow(() -> new NotFoundException("Категории с таким ID не существует"));
        categoryToUpdate.setName(categoryDto.name());
        categoryRepository.save(categoryToUpdate);
        return new CategoryDto(categoryToUpdate.getId(), categoryToUpdate.getName());
    }
}
