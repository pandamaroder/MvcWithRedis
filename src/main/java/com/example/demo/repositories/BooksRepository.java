package com.example.demo.repositories;

import com.example.demo.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BooksRepository extends JpaRepository<Book, Long>, PagingAndSortingRepository<Book, Long> {

    // List<News> findByDateCreatedBetween(LocalDate start, LocalDate end);

    Iterable<Book> findByTitleContaining(String title);

    @Override
    @EntityGraph(value = "retrieve-news-entity-with-attr", type = EntityGraph.EntityGraphType.LOAD)
    Page<Book> findAll(Pageable pageable);
}
