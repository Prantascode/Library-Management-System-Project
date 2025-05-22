package com.pranta.LibraryMangement.Entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Integer availableCopies;
    
    @Column(nullable = false)
    private Integer totalCopies;

    @Column(nullable = false)
    private LocalDateTime addedDate;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BorrowedBook> borrowedBooks = new ArrayList<>();

    @PrePersist
    protected void onCreate(){
        addedDate = LocalDateTime.now();
    }


}
