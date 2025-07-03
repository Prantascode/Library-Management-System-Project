package com.pranta.LibraryMangement.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.pranta.LibraryMangement.Entity.User;

import jakarta.validation.constraints.*;


public class DTO {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserRegistrationDto{
        @NotBlank(message = "Name is required")
        private String name;

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String password;

        private User.UserRole role = User.UserRole.STUDENT;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserResponseDto{
        private Long id;
        private String name;
        private String email;
        private User.UserRole role;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequestDto{
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "Password is required")
        private String password;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginResponseDto {
        private String email;
        private String name;
        private User.UserRole role;
        private String token;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookDto{
        private Long id;
        @NotBlank(message = "Title is required")
        private String title;
        @NotBlank(message = "Author is required")
        private String author;
        @NotBlank(message = "Category is required")
        private String category;
        @Min(value = 0, message = "Available copies cannot be negative")
        private Integer availablecopies;
        @Min(value = 1,message = "Total copies must be at least 1")
        private Integer totalcopies;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BorrowedBookDto {
        @NotNull(message = "Book ID is required")
        private Long bookId;
        @NotNull(message = "User Id is required")
        private Long userId;      
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReturnBookDto {
        @NotNull(message = "Borrowed book Id is required")
        private Long borrowedBookId;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BorrowedBookResponseDto {
        private Long id;
        private Long userId;
        private String userName;
        private Long bookId;
        private String bookTitle;
        private LocalDate issueDate;
        private LocalDate returnDate;
        private LocalDate expectedReturnDate;
        private Double fine;
        private Boolean returned;
    }
}
