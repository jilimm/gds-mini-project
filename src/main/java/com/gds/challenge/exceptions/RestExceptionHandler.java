package com.gds.challenge.exceptions;

import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {BusinessException.class})
    protected ResponseEntity<ErrorMessage> handleBusinessException(RuntimeException ex) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle exceptions thrown by failed jakarta validations
     *
     * @param ex Exception thrown by failed jakarta validation
     * @return Response Entity with Error Message
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<ErrorMessage> handleConstraintViolation(ConstraintViolationException ex) {
        List<ErrorDetail> errorDetails = ex.getConstraintViolations()
                .stream()
                .map(v -> {
                    if (v.getPropertyPath().toString().contains("<cross-parameter>")) {
                        return ErrorDetail.builder()
                                .message(v.getMessage())
                                .build();
                    } else {
                        return ErrorDetail.builder()
                                .field(v.getPropertyPath().toString().replaceAll(".*\\.", StringUtils.EMPTY))
                                .invalidValue(v.getInvalidValue().toString())
                                .message(v.getMessage())
                                .build();
                    }
                })
                .toList();
        ErrorMessage errorMessage = ErrorMessage.builder()
                .timestamp(LocalDateTime.now())
                .message(String.format("%s violations detected. Please check error details.", errorDetails.size()))
                .errorDetails(errorDetails)
                .build();
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle exceptions thrown by opencsv when parsing records as beans
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {CsvException.class})
    protected ResponseEntity<ErrorMessage> handleCsvException(CsvException ex) {
        ErrorDetail errorDetail = ErrorDetail.builder()
                .field("line " + ex.getLineNumber())
                .invalidValue(String.join(String.valueOf(CSVWriter.DEFAULT_SEPARATOR), Arrays.asList(ex.getLine())))
                .message(Optional.ofNullable(ex.getCause())
                        .map(Throwable::getMessage)
                        .orElseGet(ex::getLocalizedMessage)
                        .replaceAll("\"", "'"))
                .build();
        ErrorMessage errorMessage = ErrorMessage.builder()
                .timestamp(LocalDateTime.now())
                .message("Exception encountered when processing CSV file")
                .errorDetails(Collections.singletonList(errorDetail))
                .build();
        return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {IOException.class})
    protected ResponseEntity<ErrorMessage> handleIOException(IOException ex) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .timestamp(LocalDateTime.now())
                .message("Error occurred when reading file: "+ex.getMessage())
                .build();
        return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }



}

