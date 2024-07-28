package com.example.demo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;

@Data
@Validated
@SuperBuilder
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BookDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    @NonNull
    @EqualsAndHashCode.Include
    private String title;

    @NonNull
    @EqualsAndHashCode.Include
    private String content;

    @NonNull
    @EqualsAndHashCode.Include
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
