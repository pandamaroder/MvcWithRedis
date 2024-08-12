package com.example.demo.repositories;

import com.example.demo.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Long>, PagingAndSortingRepository<Book, Long> {

    List<Book> findByCreatedAtBetween(LocalDateTime dateStart, LocalDateTime dateEnd);

    Iterable<Book> findByTitleContaining(String title);

}
