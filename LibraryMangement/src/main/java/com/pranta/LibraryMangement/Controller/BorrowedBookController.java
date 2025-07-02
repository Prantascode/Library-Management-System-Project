package com.pranta.LibraryMangement.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pranta.LibraryMangement.DTOs.DTO.BorrowedBookDto;
import com.pranta.LibraryMangement.DTOs.DTO.BorrowedBookResponseDto;
import com.pranta.LibraryMangement.Service.BorrowedBookService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/borrowed-books")
public class BorrowedBookController {
    
    private final BorrowedBookService borrowedBookService;

    public BorrowedBookController(BorrowedBookService borrowedBookService){
        this.borrowedBookService = borrowedBookService;
    }
    @PostMapping("/borrow")
    public ResponseEntity<BorrowedBookResponseDto> borroerdBook(@Valid @RequestBody BorrowedBookDto borrowedBookDto) {
        return new ResponseEntity<>(borrowedBookService.borrowBook(borrowedBookDto),HttpStatus.CREATED);
    }
    
}
