package com.example.demo.services;

import com.example.demo.dto.BookCreateRequest;
import com.example.demo.dto.BookDto;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.model.Category;
import com.example.demo.model.Book;
import com.example.demo.repositories.BooksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BooksService extends BaseService {

    private final BooksRepository booksRepository;
   
    private final CategoryService categoryService;

    @Transactional
    public BookDto createBooks(BookCreateRequest BooksDto) {
        final Category categoryResponse = categoryService
            .createIfNeedCategory(BooksDto.getCategoryName());

        final Book book = Book.builder()
            .title(cleanData(BooksDto.getTitle()))
            .content(cleanData(BooksDto.getContent()))
            .category(categoryResponse)
            .build();

        final Book savedBooks = booksRepository.save(book);
        return BookDto.builder().id(savedBooks.getId()).categoryName(BooksDto.getCategoryName())
            .content(BooksDto.getContent())
            .title(BooksDto.getTitle())
            .build();

    }



    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "books", allEntries = true),
        @CacheEvict(value = "booksByCategory", allEntries = true)
    })
    public BookDto deleteBooks(long BooksId) {

        final Book Books = booksRepository
            .findById(BooksId)
            .orElseThrow(() -> new NotFoundException("Книги с таким ID не существует"));
        booksRepository.delete(Books);
        return BookDto.builder()
            .categoryName(Books.getCategory().getName())
            .content(Books.getContent())
            .title(Books.getTitle())
            .id(Books.getId())
            .build();
    }

    @Transactional
    @Cacheable(value = "books", key = "#title")
    public Collection<BookDto> findByTitle(String title) {
        final Iterable<Book> allBooks = booksRepository.findByTitleContaining(title);
        final Set<BookDto> bookDtos = new HashSet<>();

        for (final Book Books : allBooks) {
            bookDtos.add(BookDto.builder()
                .title(Books.getTitle())
                .categoryName(Books.getCategory().getName())
                .content(Books.getContent())
                .id(Books.getId())
                .build());
        }
        return bookDtos;

    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "books", allEntries = true),
        @CacheEvict(value = "booksByCategory", allEntries = true)
    })
    public BookDto updateBooks(BookDto bookDto) {
      

        final Book BooksToUpdate = booksRepository
            .findById(bookDto.getId())
            .orElseThrow(() -> new NotFoundException("Новости с таким ID не существует"));
        BooksToUpdate.setContent(bookDto.getContent());
        BooksToUpdate.setTitle(bookDto.getTitle());
        booksRepository.save(BooksToUpdate);
        return BookDto.builder()
            .id(bookDto.getId())
            .content(bookDto.getContent())
            .categoryName(bookDto.getCategoryName())
            .title(bookDto.getTitle())
            .build();
    }

}
