package com.pranta.LibraryMangement.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.pranta.LibraryMangement.DTOs.DTO.BookDto;
import com.pranta.LibraryMangement.Entity.Book;
import com.pranta.LibraryMangement.Exception.BadRequestException;
import com.pranta.LibraryMangement.Exception.ResourceNotFoundException;
import com.pranta.LibraryMangement.Repository.BookReporsitory;

import jakarta.transaction.Transactional;

public class BookService {
    private final BookReporsitory bookReporsitory;
    
    @Autowired
    public BookService(BookReporsitory bookReporsitory){
        this.bookReporsitory = bookReporsitory;
    }
    @Transactional
    public BookDto addBook(BookDto bookDto){
        validateBookDto(bookDto);
        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setCategory(bookDto.getCategory());
        book.setTotalCopies(bookDto.getTotalcopies());
        book.setAvailableCopies(bookDto.getAvailablecopies());
        book.setAddedDate(LocalDateTime.now());

        Book saveBook = bookReporsitory.save(book);
        return mapToDto(saveBook);
    }
    //Update Books
    public BookDto updateBook(Long bookId,BookDto bookDto){
        Book book = bookReporsitory.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: "+bookId));

        validateBookDto(bookDto);
        //Calculate the difference for available copies
        int difference = bookDto.getTotalcopies() - book.getTotalCopies();

        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setCategory(bookDto.getCategory());
        book.setTotalCopies(bookDto.getTotalcopies());

        //Update available copies accordingly
        int newAvailable = book.getAvailableCopies() + difference;
        if (newAvailable < 0) {
            throw new BadRequestException("Cannot reduce total copies below borrowed copies");
        }
        book.setAvailableCopies(newAvailable);
        Book updateBook = bookReporsitory.save(book);
        return mapToDto(updateBook);
    }
    public void deleteBook(Long bookId){
        Book book = bookReporsitory.findById(bookId)
                .orElseThrow (() -> new ResourceNotFoundException("Book not found with id : "+bookId));

        if (!book.getAvailableCopies().equals(book.getTotalCopies())) {
            throw new BadRequestException("Cannot delete book with borrowed copies");
        }
        bookReporsitory.delete(book);
    }
    public BookDto getBookById(Long bookId){
        Book book = bookReporsitory.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id : "+bookId));
        
        return mapToDto(book);
        
    }
    public List<BookDto> getAllBooks(){
        List<Book> books = bookReporsitory.findAll();
        return books.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    private void validateBookDto(BookDto bookDto) {
        if (bookDto.getTotalcopies() < 0) {
            throw new BadRequestException("Total copies cannot be negative");
        }
        if (bookDto.getAvailablecopies() != null &&
            (bookDto.getAvailablecopies() < 0 || bookDto.getAvailablecopies() > bookDto.getTotalcopies())) {
            throw new BadRequestException("Avaiable copies must be between 0 and total copies");
        }
    }
    private BookDto mapToDto(Book book){
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setCategory(book.getCategory());
        dto.setAvailablecopies(book.getAvailableCopies());
        dto.setTotalcopies(book.getTotalCopies());

        return dto;
    }
}
