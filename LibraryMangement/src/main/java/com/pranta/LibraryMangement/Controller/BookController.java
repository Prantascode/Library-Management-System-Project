package com.pranta.LibraryMangement.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pranta.LibraryMangement.DTOs.DTO.BookDto;
import com.pranta.LibraryMangement.Entity.Book;
import com.pranta.LibraryMangement.Service.BookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService){
        this.bookService = bookService;
    }
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDto> addBook (@Valid @RequestBody BookDto bookDto){
        return new ResponseEntity<>(bookService.addBook(bookDto),HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id,@Valid @RequestBody BookDto bookDto){
        return ResponseEntity.ok(bookService.updateBook(id, bookDto));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMAIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id){
        return ResponseEntity.ok(bookService.getBookById(id));
    }
}
