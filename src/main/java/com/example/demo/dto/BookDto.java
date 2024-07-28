package com.example.demo.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

import java.util.Locale;
import java.util.Objects;

@Data
@Validated
@SuperBuilder
@RequiredArgsConstructor
public class BookDto {

    private Long id;

    private String title;

    private String content;


    private String categoryName;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookDto bookDto)) {
            return false;
        }
        return Objects.equals(title, bookDto.title.trim().toLowerCase(Locale.ROOT)) &&
            Objects.equals(content, bookDto.content) &&
            Objects.equals(categoryName, bookDto.categoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, categoryName, title);
    }
}
