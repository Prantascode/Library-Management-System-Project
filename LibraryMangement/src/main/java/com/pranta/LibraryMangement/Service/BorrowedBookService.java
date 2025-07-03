package com.pranta.LibraryMangement.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

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
    
    @Value("7") // Default to 7 days borrowing period
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
        
        // Check if user already has a copy of this book
        if (borrowedBookRepository.findActiveBookBorrowByUserIdAndBookId(user.getId(), book.getId()).isPresent()) {
            throw new BadRequestException("User Already has a copy of this book");
        }
        // Check if book is available
        if (book.getAvailableCopies()<=0) {
            throw new BadRequestException("No copies of this book are currently avilable");
        }
        book.setAvailableCopies(book.getAvailableCopies()-1);
        bookReporsitory.save(book);

        // Create borrow record
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
        LocalDate returnDate = LocalDate.now();
        borrowedBook.setReturnDate(returnDate);
        borrowedBook.setReturned(true);

        LocalDate dueDate = borrowedBook.getIssueDate().plusDays(borrowingPeriodDays);
        if (returnDate.isAfter(dueDate)) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
            Double fine = daysLate * finePerDay;
            borrowedBook.setFine(fine);
        }
        // Increase Available Books
        Book book = borrowedBook.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookReporsitory.save(book);

        BorrowedBook updateBorrowedBook = borrowedBookRepository.save(borrowedBook);
        return mapToResponseDto(updateBorrowedBook);

    }
    public BorrowedBookResponseDto getBorrowedBookById(Long id){
        BorrowedBook borrowedBook = borrowedBookRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Borrowed Book not found with id : "+id));

        return mapToResponseDto(borrowedBook);
    }
    public List<BorrowedBookResponseDto> getCurrentBorrowedBookByUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id : "+userId));

        List<BorrowedBook> borrowedBooks = borrowedBookRepository.findByUserAndReturnedFalse(user);
        return borrowedBooks.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
     }
    public List<BorrowedBookResponseDto> getAllCurrentBorrowedBooks(){
        List<BorrowedBook> borrowedBooks = borrowedBookRepository.findByReturnedFalse();
        return borrowedBooks.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
     }
    public List<BorrowedBookResponseDto> getOverDueBooks(){
        LocalDate overDueDate = LocalDate.now().minusDays(borrowingPeriodDays);
        List<BorrowedBook> overDueBooks = borrowedBookRepository.findOverdueBooks(overDueDate);
        return overDueBooks.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
    public Double calculateCurrentFine(Long borrowedBookId){
        BorrowedBook borrowedBook = borrowedBookRepository.findById(borrowedBookId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowed book record not found with id : "+borrowedBookId));
        if (borrowedBook.getReturned()) {
            return borrowedBook.getFine();           
        }

        LocalDate dueDate = borrowedBook.getIssueDate().plusDays(borrowingPeriodDays);
        LocalDate currentDate = LocalDate.now();

        if (currentDate.isAfter(dueDate)) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, currentDate);
            return daysLate * finePerDay;
        }
        return 0.0;
    } 
    public BorrowedBookResponseDto mapToResponseDto(BorrowedBook borrowedBook){
        BorrowedBookResponseDto dto = new BorrowedBookResponseDto();
        dto.setId(borrowedBook.getId());
        dto.setUserId(borrowedBook.getUser().getId());
        dto.setUserName(borrowedBook.getUser().getName());
        dto.setBookId(borrowedBook.getBook().getId());
        dto.setBookTitle(borrowedBook.getBook().getTitle());
        dto.setIssueDate(borrowedBook.getIssueDate());
        dto.setReturnDate(borrowedBook.getReturnDate());
        dto.setExpectedReturnDate(borrowedBook.getIssueDate().plusDays(borrowingPeriodDays));
        dto.setFine(borrowedBook.getFine());
        dto.setReturned(borrowedBook.getReturned());

        return dto;
    } 

}
