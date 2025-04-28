package com.pranta.LibraryMangement.Model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowedBook {
    private Long id;
    private User user;
    private Book book;
    private LocalDate issuDate;
    private LocalDate returnDate;
    private Double fine;
}
