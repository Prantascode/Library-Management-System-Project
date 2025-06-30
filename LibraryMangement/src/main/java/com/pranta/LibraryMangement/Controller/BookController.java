package com.pranta.LibraryMangement.Controller;

import java.util.List;

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
import com.pranta.LibraryMangement.Service.BookService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;


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
    @GetMapping("/Search")
    public ResponseEntity<List<BookDto>> searchBook(@RequestBody String keyword){
        return ResponseEntity.ok(bookService.searchBook(keyword));
    }
    @GetMapping("/title")
    public ResponseEntity<List<BookDto>> findByTitle(@RequestParam String title) {
        return ResponseEntity.ok(bookService.findByTitle(title));
    }
    @GetMapping("/author")
    public ResponseEntity<List<BookDto>> findByAuthor(@RequestParam String author) {
        return ResponseEntity.ok(bookService.findByAuthor(author));
    }
    @GetMapping("/category")
    public ResponseEntity<List<BookDto>> findByCategory(@RequestParam String category) {
        return ResponseEntity.ok(bookService.findByCategory(category));
    }
    @GetMapping("/available")
    public ResponseEntity<List<BookDto>> findAvailableBooks() {
        return ResponseEntity.ok(bookService.findAvailableBooks());
    }
    @GetMapping("/newest")
    public ResponseEntity<List<BookDto>> findBooksByNewest() {
        return ResponseEntity.ok(bookService.findBooksByNewest());
    }
    
    @GetMapping("/most-available")
    public ResponseEntity<List<BookDto>> findBooksByMostAvaiable() {
        return ResponseEntity.ok(bookService.findBooksByMostAvailable());
    }
    
    
}
