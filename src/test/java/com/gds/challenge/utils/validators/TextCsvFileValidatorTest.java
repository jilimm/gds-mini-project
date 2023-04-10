package com.gds.challenge.utils.validators;

import com.gds.challenge.configuration.formatters.UserSortTypeEnumFormatter;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Text;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class TextCsvFileValidatorTest {

    @InjectMocks
    TextCsvFileValidator textCsvFileValidator;

    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    @Mock
    MultipartFile multipartFile;

    @Test
    void when_file_isValid() {
        when(multipartFile.getContentType()).thenReturn("text/csv");
        when(multipartFile.isEmpty()).thenReturn(false);
        assertTrue(textCsvFileValidator.isValid(multipartFile, constraintValidatorContext));
    }

    @Test
    void when_file_isEmpty_then_invalid() {
        when(multipartFile.getContentType()).thenReturn("text/csv");
        when(multipartFile.isEmpty()).thenReturn(true);
        assertFalse(textCsvFileValidator.isValid(multipartFile, constraintValidatorContext));
    }

    @Test
    void when_file_has_incorrect_contentType_then_invalid() {
        when(multipartFile.getContentType()).thenReturn("xlsx");
        when(multipartFile.isEmpty()).thenReturn(false);
        assertFalse(textCsvFileValidator.isValid(multipartFile, constraintValidatorContext));
    }

    @Test
    void when_file_null_then_invalid() {
        assertFalse(textCsvFileValidator.isValid(null, constraintValidatorContext));
    }
}