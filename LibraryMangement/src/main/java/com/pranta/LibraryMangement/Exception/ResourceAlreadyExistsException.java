package com.pranta.LibraryMangement.Exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceAlreadyExistsException extends RuntimeException{
    public ResourceAlreadyExistsException(String message){
        super(message);
    }
}