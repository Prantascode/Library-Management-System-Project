package com.pranta.LibraryMangement.GlobalExceptionHandeler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.pranta.LibraryMangement.Exception.AuthenticationException;
import com.pranta.LibraryMangement.Exception.BadRequestException;
import com.pranta.LibraryMangement.Exception.ResourceAlreadyExistsException;
import com.pranta.LibraryMangement.Exception.ResourceNotFoundException;

@ControllerAdvice
public class GlobalException {

   @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(
        ResourceNotFoundException exception,
        WebRequest webRequest) {

        ErrorDetails errorDetails = new ErrorDetails(
            LocalDateTime.now(),
            exception.getMessage(),
            webRequest.getDescription(false),
            "RESOURCE_NOT_FOUND"
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDetails> handleBadRequestException(
        BadRequestException exception,
        WebRequest webRequest){

            ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false),
                "BAD_REQUEST"
            );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> handleResourceAlreadyExistsException(
        ResourceAlreadyExistsException exception,
        WebRequest webRequest){

            ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false),
                "RESOURCE_ALREADY_EXISTS"
            );
            return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorDetails> handleAuthenticationException(
        AuthenticationException exception,
        WebRequest webRequest){

            ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false),
                "AUTHENTICATION_FAILED"
            );
            return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
        }
}
