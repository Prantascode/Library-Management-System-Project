package com.pranta.LibraryMangement.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.pranta.LibraryMangement.DTOs.DTO.LogicResponseDto;
import com.pranta.LibraryMangement.DTOs.DTO.LoginRequestDto;
import com.pranta.LibraryMangement.DTOs.DTO.UserRegistrationDto;
import com.pranta.LibraryMangement.DTOs.DTO.UserResponseDto;
import com.pranta.LibraryMangement.Service.AuthService;
import com.pranta.LibraryMangement.Service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class AuthController {
    
    private final AuthService authService;
    private final UserService userService;

    @Autowired
    public AuthController (AuthService authService,UserService userService){
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LogicResponseDto> login( @Valid @RequestBody LoginRequestDto loginRequest) {
        LogicResponseDto loginResponse = authService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }
    
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRegistrationDto registrationDto) {
        UserResponseDto userResponse = userService.registerUser(registrationDto);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }
}
