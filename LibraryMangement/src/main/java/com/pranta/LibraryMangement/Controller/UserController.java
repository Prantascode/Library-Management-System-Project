package com.pranta.LibraryMangement.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pranta.LibraryMangement.DTOs.DTO.UserResponseDto;
import com.pranta.LibraryMangement.Service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
   public final UserService userService;

   @Autowired
   public UserController(UserService userService){
        this.userService = userService;
   }
   @GetMapping("/{id}")
   public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
   }
   @GetMapping("/students")
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<List<UserResponseDto>> getAllStudents(){
        return ResponseEntity.ok(userService.getAllStudents());
   }
}
