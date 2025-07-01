package com.pranta.LibraryMangement.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.pranta.LibraryMangement.Entity.User;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
    List<User> findAllByRole(User.UserRole role);
    boolean existsByEmail(String email);
}
