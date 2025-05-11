package com.pranta.LibraryMangement.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pranta.LibraryMangement.Entity.User;
import com.pranta.LibraryMangement.Model.BorrowedBook;

@Repository
public interface BorrowedBookRepository extends JpaRepository<BorrowedBook, Long> {
    List<BorrowedBook> findByUserAndReturnedFalse(User user);
    List<BorrowedBook> findBYReturnedFalse();
    @Query("SELECT bb FROM BorrowedBook bb WHERE bb.user.id = :userId AND bb.book.id = :bookId AND bb.returned = false")
    Optional<BorrowedBook> findActiveBookBorrowByIdAndBookId(
        @Param("userId") Long userId,
        @Param("bookId") Long bookId
    );
    @Query("SELECT bb FROM BorrowedBook bb WHERE bb.issueDate < :date AND bb.returned = false")
    List<BorrowedBook> findOverdueBooks(@Param("date") LocalDate date);

} 