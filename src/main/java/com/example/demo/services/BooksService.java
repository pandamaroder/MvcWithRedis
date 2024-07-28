package com.example.demo.services;

import com.example.demo.dto.BookCreateRequest;
import com.example.demo.dto.BookDto;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.model.Book;
import com.example.demo.model.Category;
import com.example.demo.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BooksService extends BaseService {

    private final BooksRepository booksRepository;
    private final CategoryService categoryService;
    private final int period;

    @Autowired
    private CacheManager cacheManager;

    public BooksService(BooksRepository booksRepository, CategoryService categoryService, @Value("${books.periodForNewBooks}") int period) {
        this.booksRepository = booksRepository;
        this.categoryService = categoryService;
        this.period = period;
    }

    @Cacheable(value = "books", key = "#onDate.toString()")
    public List<BookDto> getNewBooks(LocalDate onDate) {
        final LocalDateTime endOfDay = onDate.atTime(LocalTime.now(ZoneId.systemDefault()));
        final LocalDateTime startOfPeriod = endOfDay.minusDays(period);

        final List<Book> books = booksRepository.findByCreatedAtBetween(startOfPeriod, endOfDay);
        return books.stream().map(book -> BookDto
            .builder().id(book.getId())
            .title(book.getTitle())
            .content(book.getContent())
            .categoryName(book.getCategory()
                .getName()).build())
            .collect(Collectors.toList());
    }

    @Transactional
    @CachePut(value = "booksByCategory", key = "#result.categoryName")
    public BookDto createBooks(BookCreateRequest bookDto) {
        final Category categoryResponse = categoryService.createIfNeedCategory(bookDto.getCategoryName());

        final Book book = Book.builder()
            .title(cleanData(bookDto.getTitle()))
            .content(cleanData(bookDto.getContent()))
            .category(categoryResponse)
            .build();

        final Book savedBook = booksRepository.save(book);

        final BookDto bookDto1 = BookDto.builder()
            .id(savedBook.getId())
            .categoryName(bookDto.getCategoryName())
            .content(bookDto.getContent())
            .title(bookDto.getTitle())
            .build();

        // Обновляем основной кеш Books
        updateBooksCache(bookDto1);

        return bookDto1;
    }

    @Caching(
        evict = {
            @CacheEvict(value = "booksByCategory", key = "#result.categoryName")}
    )
    public BookDto deleteBooks(long bookdId) {

        final Book book = booksRepository.findById(bookdId).orElseThrow(() -> new NotFoundException("Книги с таким ID не существует"));
        booksRepository.delete(book);

        return BookDto.builder()
            .categoryName(book.getCategory().getName())
            .content(book.getContent())
            .title(book.getTitle())
            .id(book.getId())
            .build();
    }

    @Transactional
    //@Cacheable(value = "books", key = "#title")
    public Collection<BookDto> findByTitle(String title) {
        final Iterable<Book> allBooks = booksRepository.findByTitleContaining(title);
        final Set<BookDto> bookDtos = new HashSet<>();

        for (final Book book : allBooks) {
            bookDtos.add(BookDto
                .builder()
                .title(book.getTitle())
                .categoryName(book.getCategory()
                    .getName()).content(book.getContent())
                .id(book.getId())
                .build());
        }
        return bookDtos;

    }

    @Transactional
    @CachePut(value = "booksByCategory", key = "#result.categoryName")
     public BookDto updateBooks(BookDto bookDto) {
        final Book bookUpdate = booksRepository
            .findById(bookDto.getId())
            .orElseThrow(() -> new NotFoundException("Книга с таким ID не существует"));

        bookUpdate.setContent(bookDto.getContent());
        bookUpdate.setTitle(bookDto.getTitle());

        booksRepository.save(bookUpdate);

        return BookDto.builder()
            .id(bookDto.getId())
            .content(bookDto.getContent())
            .categoryName(bookDto.getCategoryName())
            .title(bookDto.getTitle()).build();
    }

    private void updateBooksCache(BookDto newBook) {
        final Cache booksCache = cacheManager.getCache("books");
        if (booksCache != null) {
            final LocalDate today = LocalDate.now(ZoneId.systemDefault());
            final String cacheKey = today.toString();
            final Cache.ValueWrapper valueWrapper = booksCache.get(cacheKey);

            List<BookDto> cachedBooks;
            if (valueWrapper != null) {
                cachedBooks = (List<BookDto>) valueWrapper.get();
            } else {
                cachedBooks = new ArrayList<>();
            }

            cachedBooks.add(newBook);
            booksCache.put(cacheKey, cachedBooks);
        }
    }
}
