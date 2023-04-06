package com.gds.challenge.exceptions;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {

    private HttpStatus httpStatus;
    public BusinessException(String message) {
        super(message);
    }
}

