package com.gds.challenge.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    // https://www.toptal.com/java/spring-boot-rest-api-error-handling

    @ExceptionHandler(value = {BusinessException.class})
    protected ResponseEntity<ErrorMessage> handleBusinessException(RuntimeException ex, WebRequest request) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle exceptions thrown by failed jakarta validations
     *
     * @param ex      Exception thrown by failed jakarta validation
     * @param request request
     * @return Response Entity with Error Message
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<ErrorMessage> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        System.out.println("here");
        // constraint violations for 1
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
        return new ResponseEntity<>(errorMessage,  HttpStatus.BAD_REQUEST);
    }


    // TODO: Handle CSV exception

}

