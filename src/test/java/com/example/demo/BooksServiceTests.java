package com.example.demo;

import com.example.demo.dto.BookDto;
import com.example.demo.helpers.DataHelper;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.services.BooksService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class BooksServiceTests extends TestBase {

    @Autowired
    private BooksService bookService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void createBooks() {
        final int countBooksBefore = getEntriesCount(TABLE_CATEGORIES);
        final int countNewsBefore = getEntriesCount(TABLE_BOOKS);

        final String categoryName = DataHelper.getAlphabeticString(10);
        final BookDto news = createBookForTest(categoryName, "testTitle", "Belochkin");

        final int countBooksAfter = getEntriesCount(TABLE_CATEGORIES);
        final int countNewsAfter = getEntriesCount(TABLE_BOOKS);

        assertThat(countBooksAfter - countBooksBefore)
            .isOne();
        assertThat(countNewsAfter - countNewsBefore)
            .isOne();
        assertThat(categoryRepository.findByName(categoryName))
            .isPresent();

        assertThat(news)
            .isNotNull();
        assertThat(news.getId())
            .isPositive();
        assertThat(news.getCategoryName())
            .isEqualTo(categoryName);
        assertThat(news.getContent())
            .isEqualTo(categoryName);
    }
}
