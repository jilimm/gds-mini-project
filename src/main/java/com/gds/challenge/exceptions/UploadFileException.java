package com.gds.challenge.exceptions;

public class UploadFileException extends RuntimeException {

    public UploadFileException(String message, Exception cause) {
        super(message, cause);
    }
}

