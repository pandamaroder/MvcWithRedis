package com.example.demo;

import com.example.demo.dto.BookDto;
import com.example.demo.dto.CategoryCreateRequest;
import com.example.demo.dto.CategoryCreateResponse;
import com.example.demo.helpers.DataHelper;
import com.example.demo.services.BooksService;
import com.example.demo.services.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BooksCacheTest extends TestBase {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BooksService bookService;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void putBasicKeyIntoCacheWithRedisTemplate() {
        final String key = "testKey";
        final String value = "testValue";

        final ValueOperations<String, String> valueOps = redisTemplate.opsForValue();

        valueOps.set(key, value);
        final String cachedValue = valueOps.get(key);

        assertThat(cachedValue).isEqualTo(value);
    }

    @Test
    void updateBooksUpdateBooksByCategoryCache() {
        // Given:
        final int countBooksBefore = getEntriesCount(TABLE_BOOKS);
        final String categoryName = DataHelper.getAlphabeticString(10);

        final CategoryCreateResponse testCategory = categoryService.createCategory(new CategoryCreateRequest("test"));
        final BookDto book = createBookForTest(categoryName, "testTitleee", "Belochkinnn");

        final var bookDto = BookDto.builder().id(book.getId()).categoryName(testCategory.name()).content("content").title("title").build();
        // When: вызов метода updateBooks
        bookService.updateBooks(bookDto);
        final int countBooksAfter = getEntriesCount(TABLE_BOOKS);
        final Cache booksCache = cacheManager.getCache("books");
        final Cache booksByCategoryCache = cacheManager.getCache("booksByCategory");
        assertThat(countBooksAfter - countBooksBefore).isOne();
        assertThat(booksCache).isNotNull(); // не понятно откуда этот кеш берется - вызова пр котором создается этот кеш не произв
        assertThat(booksByCategoryCache).isNotNull();
        final Cache.ValueWrapper valueWrapper = booksByCategoryCache.get(book.getCategoryName());

        final BookDto cachedBooks = (BookDto) valueWrapper.get();
        assertThat(cachedBooks).isNotNull();

        final BookDto cachedBookDto = booksByCategoryCache.get(book.getCategoryName(), BookDto.class);

        assertThat(cachedBookDto).isNotNull();
        assertThat(cachedBookDto.getTitle()).isEqualTo(book.getTitle());
        assertThat(cachedBookDto.getContent()).isEqualTo(book.getContent());
        assertThat(cachedBookDto).usingRecursiveComparison().isEqualTo(book);
    }

    @Test
    void addBooksUpdateBooksCache() {
        // Given:
        final LocalDate onDate = LocalDate.now(ZoneId.systemDefault());

        final String categoryName = DataHelper.getAlphabeticString(10);
        final int countBooksBefore = getEntriesCount(TABLE_BOOKS);
        createBookForTest(categoryName, "testTitle4", "Belochkin");
        createBookForTest(categoryName, "testTitle", "Belochkin4");
        final int countBooksAfter = getEntriesCount(TABLE_BOOKS);

        final Cache booksCache = cacheManager.getCache("books");

        assertThat(countBooksAfter - countBooksBefore).isEqualTo(2);
        assertThat(booksCache).isNotNull();

        final Cache.ValueWrapper valueWrapper = booksCache.get(onDate.toString());

        assertThat(valueWrapper).isNotNull();

        final List<BookDto> cachedBooks = (List<BookDto>) valueWrapper.get();
        assertThat(cachedBooks).isNotNull();
        assertThat(cachedBooks).hasSize(2);
        final List<BookDto> newBooks = bookService.getNewBooks(onDate);
        assertThat(cachedBooks).usingRecursiveComparison().isEqualTo(newBooks);
    }

    @Test
    void lastUpdatesFromBooksCache() {
        createBookForTest("test", "testTitle", "Belochkin");
        final LocalDate onDate = LocalDate.now(ZoneId.systemDefault());

        final int countBooksAfter = getEntriesCount(TABLE_BOOKS);

        final List<BookDto> newBooks = bookService.getNewBooks(onDate);
        final Cache booksCache = cacheManager.getCache("books");

        assertThat(countBooksAfter).isEqualTo(1);
        assertThat(booksCache).isNotNull();

        final Cache.ValueWrapper valueWrapper = booksCache.get(onDate.toString());

        assertThat(valueWrapper).isNotNull();

        final List<BookDto> cachedBooks = (List<BookDto>) valueWrapper.get();
        assertThat(cachedBooks).isNotNull();
        assertThat(cachedBooks).hasSize(1);
        assertThat(cachedBooks).usingRecursiveComparison().isEqualTo(newBooks);
    }

    @Test
    void addBooksUpdateBooksByCategoryCache() {
        // Given:
        final String categoryName = DataHelper.getAlphabeticString(10);
        final int countBooksBefore = getEntriesCount(TABLE_BOOKS);
        final BookDto book = createBookForTest(categoryName, "testTitle", "Belochkin");

        final int countBooksAfter = getEntriesCount(TABLE_BOOKS);

        final Cache booksCache = cacheManager.getCache("books");
        final Cache booksByCategoryCache = cacheManager.getCache("booksByCategory");
        assertThat(countBooksAfter - countBooksBefore).isOne();
        assertThat(booksCache).isNotNull(); // не понятно откуда этот кеш берется - вызова пр котором создается этот кеш не произв
        assertThat(booksByCategoryCache).isNotNull();
        final Cache.ValueWrapper valueWrapper = booksByCategoryCache.get(book.getCategoryName());

        final BookDto cachedBooks = (BookDto) valueWrapper.get();
        assertThat(cachedBooks).isNotNull();

        final BookDto cachedBookDto = booksByCategoryCache.get(book.getCategoryName(), BookDto.class);

        assertThat(cachedBookDto).isNotNull();
        assertThat(cachedBookDto.getTitle()).isEqualTo(book.getTitle());
        assertThat(cachedBookDto.getContent()).isEqualTo(book.getContent());
        assertThat(cachedBookDto).usingRecursiveComparison().isEqualTo(book);
    }

}
