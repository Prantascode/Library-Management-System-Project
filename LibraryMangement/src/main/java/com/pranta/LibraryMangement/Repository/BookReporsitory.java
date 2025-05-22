package com.pranta.LibraryMangement.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pranta.LibraryMangement.Entity.Book;

@Repository
public interface BookReporsitory extends JpaRepository<Book, Long> {
    List<Book> findByTitleContaining(String title);
    List<Book> findByAuthor(String author);
    List<Book> findByCategory(String category);

     @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
           + "LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
           + "LOWER(b.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
           List<Book> searchBooks(@Param("keyword") String keyword);

           List<Book> findByAvailableCopiesGreaterThan(Integer count);
           List<Book> findAllByOrderByAddedDateDesc();
           List<Book> findAllByOrderByAvailableCopiesDesc();
}
