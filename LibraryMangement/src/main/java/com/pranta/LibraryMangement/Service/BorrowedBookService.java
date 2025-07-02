package com.pranta.LibraryMangement.Service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pranta.LibraryMangement.Exception.BadRequestException;
import com.pranta.LibraryMangement.DTOs.DTO.BorrowedBookDto;
import com.pranta.LibraryMangement.DTOs.DTO.BorrowedBookResponseDto;
import com.pranta.LibraryMangement.DTOs.DTO.ReturnBookDto;
import com.pranta.LibraryMangement.Entity.Book;
import com.pranta.LibraryMangement.Entity.BorrowedBook;
import com.pranta.LibraryMangement.Entity.User;
import com.pranta.LibraryMangement.Exception.ResourceNotFoundException;
import com.pranta.LibraryMangement.Repository.BookReporsitory;
import com.pranta.LibraryMangement.Repository.BorrowedBookRepository;
import com.pranta.LibraryMangement.Repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class BorrowedBookService {
    
    private final  BorrowedBookRepository borrowedBookRepository;
    private final BookReporsitory bookReporsitory;
    private final UserRepository userRepository;

    @Value("${library.fine.per-day}")
    private double finePerDay;

    @Value("7")
    private int borrowingPeriodDays;

    @Autowired
    public BorrowedBookService(BorrowedBookRepository borrowedBookRepository,BookReporsitory bookReporsitory,UserRepository userRepository){
        this.borrowedBookRepository = borrowedBookRepository;
        this.bookReporsitory = bookReporsitory;
        this.userRepository = userRepository;
    }

    @Transactional
    public BorrowedBookResponseDto borrowBook(BorrowedBookDto borrowedBookDto){
        User user = userRepository.findById(borrowedBookDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id : "+ borrowedBookDto.getUserId()));

        Book book = bookReporsitory.findById(borrowedBookDto.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id : "+borrowedBookDto.getBookId()));
        
        if (borrowedBookRepository.findActiveBookBorrowByIdAndBookId(user.getId(), book.getId()).isPresent()) {
            throw new BadRequestException("User Already has a copy of this book");
        }
        if (book.getAvailableCopies()<=0) {
            throw new BadRequestException("No copies of this book are currently avilable");
        }
        book.setAvailableCopies(book.getAvailableCopies()-1);
        bookReporsitory.save(book);

        BorrowedBook borrowedBook = new BorrowedBook();
        borrowedBook.setUser(user);
        borrowedBook.setBook(book);
        borrowedBook.setIssueDate(LocalDate.now());
        borrowedBook.setReturned(false);
        borrowedBook.setFine(0.0);

        BorrowedBook saveBorrowedBook = borrowedBookRepository.save(borrowedBook);
        return mapToResponseDto(saveBorrowedBook);
    }
    @Transactional
    public BorrowedBookResponseDto returnBook(ReturnBookDto returnBookDto){
        BorrowedBook borrowedBook = borrowedBookRepository.findById(returnBookDto.getBorrowedBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Borrowed Book record not found with id : "+returnBookDto.getBorrowedBookId()));

        if (borrowedBook.getReturned()) {
            throw new BadRequestException("Book has Already Retruned");
        }
        //Calculate fine if overdue
        


    }

    public BorrowedBookResponseDto mapToResponseDto(BorrowedBook borrowedBook){
        BorrowedBookResponseDto dto = new BorrowedBookResponseDto();
        dto.setId(borrowedBook.getId());
        dto.setUserId(borrowedBook.getUser().getId());
        dto.setUserName(borrowedBook.getUser().getName());
        dto.setBookId(borrowedBook.getBook().getId());
        dto.setBookTitle(borrowedBook.getBook().getTitle());
        dto.setIssuDate(borrowedBook.getIssueDate());
        dto.setReturnDate(borrowedBook.getReturnDate());
        dto.setFine(borrowedBook.getFine());
        dto.setReturned(borrowedBook.getReturned());

        return dto;
    } 
}
