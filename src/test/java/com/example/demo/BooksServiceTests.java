package com.example.demo;

import com.example.demo.dto.BookDto;
import com.example.demo.helpers.DataHelper;
import com.example.demo.model.Book;
import com.example.demo.repositories.BooksRepository;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.services.BooksService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class BooksServiceTests extends TestBase {

    @Autowired
    private BooksService bookService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BooksRepository booksRepository;

    @Test
    void createBooks() {
        final int countBooksBefore = getEntriesCount(TABLE_CATEGORIES);
        final int countbookBefore = getEntriesCount(TABLE_BOOKS);

        final String categoryName = DataHelper.getAlphabeticString(10);
        final BookDto book = createBookForTest(categoryName, "testTitle", "Belochkin");

        final int countBooksAfter = getEntriesCount(TABLE_CATEGORIES);
        final int countbookAfter = getEntriesCount(TABLE_BOOKS);

        assertThat(countBooksAfter - countBooksBefore)
                .isOne();
        assertThat(countbookAfter - countbookBefore)
                .isOne();
        assertThat(categoryRepository.findByName(categoryName))
                .isPresent();

        assertThat(book)
                .isNotNull();
        assertThat(book.getId())
                .isPositive();
        assertThat(book.getCategoryName())
                .isEqualTo(categoryName);
        assertThat(book.getContent())
                .isEqualTo(categoryName);
    }

    @Test
    void createBooksEntityTimeCreationCheck() {
        final int countBooksBefore = getEntriesCount(TABLE_CATEGORIES);
        final int countbookBefore = getEntriesCount(TABLE_BOOKS);

        final String categoryName = DataHelper.getAlphabeticString(10);
        final BookDto bookForTest = createBookForTest(categoryName, "testTitle", "Belochkin");

        final int countBooksAfter = getEntriesCount(TABLE_CATEGORIES);
        final int countbookAfter = getEntriesCount(TABLE_BOOKS);
        final Optional<Book> bookEntity = booksRepository.findById(bookForTest.getId());
        assertThat(countBooksAfter - countBooksBefore)
                .isOne();
        assertThat(countbookAfter - countbookBefore)
                .isOne();
        assertThat(categoryRepository.findByName(categoryName))
                .isPresent();

        assertThat(bookEntity).isPresent();

        final LocalDateTime fixedNow = LocalDateTime.now(mutableClock);
        //  чтобы учесть небольшую задержку
        //  между моментом создания записи в базе и моментом выполнения проверки в тесте.
        //проверяя, что время создания записи (createdAt) находится
        // в пределах последних 10 секунд до текущего времени (now).
        assertThat(bookEntity.get().getCreatedAt())
                .isBeforeOrEqualTo(fixedNow)
                .isAfter(fixedNow.minusSeconds(10));
    }
}
