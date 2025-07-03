package com.pranta.LibraryMangement.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pranta.LibraryMangement.DTOs.DTO.BorrowedBookDto;
import com.pranta.LibraryMangement.DTOs.DTO.BorrowedBookResponseDto;
import com.pranta.LibraryMangement.DTOs.DTO.ReturnBookDto;
import com.pranta.LibraryMangement.Service.BorrowedBookService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/borrowed-books")
public class BorrowedBookController {
    
    private final BorrowedBookService borrowedBookService;

    @Autowired
    public BorrowedBookController(BorrowedBookService borrowedBookService){
        this.borrowedBookService = borrowedBookService;
    }
    @PostMapping("/borrow")
    public ResponseEntity<BorrowedBookResponseDto> borrowBook(@Valid @RequestBody BorrowedBookDto borrowedBookDto) {
        return new ResponseEntity<>(borrowedBookService.borrowBook(borrowedBookDto),HttpStatus.CREATED);
    }
    @PostMapping("/return")
    public ResponseEntity<BorrowedBookResponseDto> returnBook(@Valid @RequestBody ReturnBookDto returnBookDto) {
        return  ResponseEntity.ok(borrowedBookService.returnBook(returnBookDto));
    }
    @GetMapping("/{id}")
    public ResponseEntity<BorrowedBookResponseDto> getBorrowedBookById(@PathVariable Long id) {
        return ResponseEntity.ok(borrowedBookService.getBorrowedBookById(id));
    }   
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BorrowedBookResponseDto>> getCurrentBorrowedBookByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(borrowedBookService.getCurrentBorrowedBookByUser(userId));
    }
    @GetMapping("/current")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BorrowedBookResponseDto>> getAllCurrentBorrowedBooks() {
        return ResponseEntity.ok(borrowedBookService.getAllCurrentBorrowedBooks());
    }
    @GetMapping("/overdue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BorrowedBookResponseDto>> getOverDueBooks() {
        return ResponseEntity.ok(borrowedBookService.getOverDueBooks());
    }
    @GetMapping("/{id}/fine")
    public ResponseEntity<Double> calculateCurrentFine(@PathVariable Long id){
        return ResponseEntity.ok(borrowedBookService.calculateCurrentFine(id));
    } 
}
