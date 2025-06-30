package com.pranta.LibraryMangement.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends RuntimeException{
    public AuthenticationException(String massage){
        super(massage);
    }
}
