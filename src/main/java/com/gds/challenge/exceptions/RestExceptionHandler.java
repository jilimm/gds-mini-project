package com.gds.challenge.exceptions;

import com.gds.challenge.model.GenericResponse;
import com.gds.challenge.model.error.ErrorDetail;
import com.gds.challenge.model.error.ErrorMessage;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
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
    protected ResponseEntity<GenericResponse> handleBusinessException(RuntimeException ex) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .build();
        GenericResponse genericResponse = GenericResponse.builder().error(errorMessage).build();
        return new ResponseEntity<GenericResponse>(genericResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle exceptions thrown by failed jakarta validations
     *
     * @param ex Exception thrown by failed jakarta validation
     * @return Response Entity with Error Message
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<GenericResponse> handleConstraintViolation(ConstraintViolationException ex) {
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
                .details(errorDetails)
                .build();
        return new ResponseEntity<>(GenericResponse.builder().error(errorMessage).build(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = {UploadFileException.class})
    protected ResponseEntity<GenericResponse> handleUploadFileExceptions(UploadFileException e) {
        GenericResponse.GenericResponseBuilder fileUploadResponseBuilder = GenericResponse.builder().success(0);
        Throwable exceptionCause = e.getCause();
        ErrorMessage.ErrorMessageBuilder errorMessageBuilder = ErrorMessage.builder();
        if (exceptionCause instanceof CsvException) {
            CsvException ex = (CsvException) exceptionCause;
            ErrorDetail errorDetail = ErrorDetail.builder()
                    .field("line " + ex.getLineNumber())
                    .invalidValue(String.join(String.valueOf(CSVWriter.DEFAULT_SEPARATOR), Arrays.asList(ex.getLine())))
                    .message(Optional.ofNullable(ex.getCause())
                            .map(Throwable::getMessage)
                            .orElseGet(ex::getLocalizedMessage)
                            .replaceAll("\"", "'"))
                    .build();
            errorMessageBuilder
                    .timestamp(LocalDateTime.now())
                    .message(e.getMessage())
                    .details(Collections.singletonList(errorDetail));
            fileUploadResponseBuilder.error(errorMessageBuilder.build());
            return new ResponseEntity<GenericResponse>(fileUploadResponseBuilder.build(), HttpStatus.BAD_REQUEST);
        }
        if (exceptionCause instanceof  IOException) {
            errorMessageBuilder
                    .timestamp(LocalDateTime.now())
                    .message(exceptionCause.getMessage());
            fileUploadResponseBuilder.error(errorMessageBuilder.build());
            return new ResponseEntity<GenericResponse>(fileUploadResponseBuilder.build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        fileUploadResponseBuilder.error(errorMessageBuilder.build());
        return new ResponseEntity<GenericResponse>(fileUploadResponseBuilder.build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }



}

