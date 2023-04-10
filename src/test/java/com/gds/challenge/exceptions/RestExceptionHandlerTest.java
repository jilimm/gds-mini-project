package com.gds.challenge.exceptions;

import com.gds.challenge.model.GenericResponse;
import com.gds.challenge.model.error.ErrorDetail;
import com.gds.challenge.model.error.ErrorMessage;
import com.gds.challenge.utils.UserSortType;
import com.opencsv.exceptions.CsvException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class RestExceptionHandlerTest {

    private static final String EXCEPTION_MESSAGE = "Lorem Ipsum";
    @InjectMocks
    RestExceptionHandler restExceptionHandler;
    @Mock
    MethodArgumentTypeMismatchException methodArgumentTypeMismatchException;

    @Test
    void handleBusinessException() {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        BusinessException businessException = new BusinessException(httpStatus, EXCEPTION_MESSAGE);
        ResponseEntity<GenericResponse> responseEntity = restExceptionHandler.handleBusinessException(businessException);

        assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getStatusCode().value());
        assertEquals(EXCEPTION_MESSAGE, Optional.ofNullable(responseEntity.getBody())
                .map(GenericResponse::getError)
                .map(ErrorMessage::getMessage)
                .orElse(null));
    }

    @Test
    void methodArgumentTypeMismatchException_from_conversionFailedException() {
        ConversionFailedException conversionFailedException =
                new ConversionFailedException(null, TypeDescriptor.valueOf(UserSortType.class), null, new Exception());
        when(methodArgumentTypeMismatchException.getCause()).thenReturn(conversionFailedException);

        ResponseEntity<GenericResponse> responseEntity = restExceptionHandler
                .methodArgumentTypeMismatchException(methodArgumentTypeMismatchException);
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getStatusCode().value());
        assertEquals("Incorrect value given", Optional.ofNullable(responseEntity.getBody())
                .map(GenericResponse::getError)
                .map(ErrorMessage::getMessage)
                .orElse(null));
        assertTrue(Optional.ofNullable(responseEntity.getBody())
                .map(GenericResponse::getError)
                .map(ErrorMessage::getDetails)
                .filter(detailList -> detailList.size() > 0)
                .map(detailList -> detailList.get(0))
                .map(ErrorDetail::getMessage)
                .map(detailString -> detailString.contains(Arrays.toString(UserSortType.values())))
                .orElse(false));
    }

    @Test
    void methodArgumentTypeMismatchException_from_NumberFormatException() {
        NumberFormatException numberFormatException = new NumberFormatException();
        when(methodArgumentTypeMismatchException.getCause()).thenReturn(numberFormatException);

        ResponseEntity<GenericResponse> responseEntity = restExceptionHandler
                .methodArgumentTypeMismatchException(methodArgumentTypeMismatchException);
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getStatusCode().value());
        assertEquals("Incorrect value given", Optional.ofNullable(responseEntity.getBody())
                .map(GenericResponse::getError)
                .map(ErrorMessage::getMessage)
                .orElse(null));
    }

    @Test
    void handleConstraintViolation() {
        ConstraintViolation<?> constraintViolation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(constraintViolation.getPropertyPath()).thenReturn(path);
        when(path.toString()).thenReturn("User<cross-parameter>");
        when(constraintViolation.getMessage()).thenReturn(EXCEPTION_MESSAGE);
        ConstraintViolationException constraintViolationException = mock(ConstraintViolationException.class);
        Set<ConstraintViolation<?>> constraintViolationSet = new HashSet<>();
        constraintViolationSet.add(constraintViolation);
        when(constraintViolationException.getConstraintViolations()).thenReturn(constraintViolationSet);

        ResponseEntity<GenericResponse> responseEntity = restExceptionHandler.handleConstraintViolation(constraintViolationException);

        assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getStatusCode().value());
        assertTrue(Optional.ofNullable(responseEntity.getBody())
                .map(GenericResponse::getError)
                .map(ErrorMessage::getMessage)
                .map(s -> s.contains("1 violations detected. Please check error details."))
                .orElse(false));

        assertTrue(Optional.ofNullable(responseEntity.getBody())
                .map(GenericResponse::getError)
                .map(ErrorMessage::getDetails)
                .filter(list -> list.size() > 0)
                .map(list -> list.get(0))
                .map(ErrorDetail::getMessage)
                .map(EXCEPTION_MESSAGE::equals)
                .orElse(false));


    }

    @Test
    void handleUploadFileExceptions_from_CsvException() {
        final long EXPECTED_LINE_NUMBER = 6;
        final String[] EXPECTED_LINE = new String[]{"col1", "col2"};

        CsvException csvException = mock(CsvException.class);
        when(csvException.getLine()).thenReturn(EXPECTED_LINE);
        when(csvException.getLineNumber()).thenReturn(EXPECTED_LINE_NUMBER);
        when(csvException.getCause()).thenReturn(new Exception(EXCEPTION_MESSAGE));

        UploadFileException uploadFileException = new UploadFileException("testString", csvException);

        ResponseEntity<GenericResponse> responseEntity = restExceptionHandler.handleUploadFileExceptions(uploadFileException);

        assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getStatusCode().value());
        assertEquals(0, Optional.ofNullable(responseEntity.getBody())
                .map(GenericResponse::getSuccess)
                .orElse(null));
        assertEquals("testString", Optional.ofNullable(responseEntity.getBody())
                .map(GenericResponse::getError)
                .map(ErrorMessage::getMessage)
                .orElse(null));

        assertEquals(EXCEPTION_MESSAGE, Optional.ofNullable(responseEntity.getBody())
                .map(GenericResponse::getError)
                .map(ErrorMessage::getDetails)
                .filter(list -> list.size() > 0)
                .map(list -> list.get(0))
                .map(ErrorDetail::getMessage)
                .orElse(null));

        assertTrue(Optional.ofNullable(responseEntity.getBody())
                .map(GenericResponse::getError)
                .map(ErrorMessage::getDetails)
                .filter(list -> list.size() > 0)
                .map(list -> list.get(0))
                .map(ErrorDetail::getField)
                .filter(StringUtils::isNotBlank)
                .map(field -> field.contains(String.valueOf(EXPECTED_LINE_NUMBER)))
                .orElse(false));


    }
}