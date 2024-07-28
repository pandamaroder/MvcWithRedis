package com.example.demo.controller;

import com.example.demo.TestBase;
import com.example.demo.dto.BookDto;
import com.example.demo.dto.CategoryCreateRequest;
import com.example.demo.dto.CategoryCreateResponse;
import com.example.demo.helpers.DataHelper;
import com.example.demo.services.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

class BooksControllerTest extends TestBase {

    @Autowired
    private CategoryService categoryService;

    @Test
    void updateBooks() {
        final String categoryName = DataHelper.getAlphabeticString(10);

        final CategoryCreateResponse testCategory = categoryService.createCategory(new CategoryCreateRequest("test"));
        final BookDto book = createBookForTest(categoryName, "testTitle", "Belochkin");

        final var bookDto = BookDto.builder()
            .id(book.getId())
            .categoryName(testCategory.name())
            .content("content")
            .title("title")
            .build();

        webTestClient.put()
            .uri("/books")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(bookDto)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(BookDto.class)
            .returnResult();

        assertThat(bookDto)
            .isNotNull();
    }
}
