package com.gds.challenge.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UploadFileException extends RuntimeException {

    private HttpStatus httpStatus;
    private Exception underlyingException;
    public UploadFileException(String message, Exception cause) {
        super(message);
        this.underlyingException = cause;
    }
}

