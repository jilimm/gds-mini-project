package com.gds.challenge.utils.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;


/**
 * Validator for MultipartForm inputs
 * Ensure file is not empty and media type is csv
 */
public class TextCsvFileValidator implements ConstraintValidator<TextCsvFile, MultipartFile> {

    @Override
    public void initialize(TextCsvFile constraintAnnotation) {

    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {

        if (multipartFile == null) {
            return false;
        }
        return isSupportedContentType(multipartFile.getContentType()) && !multipartFile.isEmpty();

    }

    private boolean isSupportedContentType(String contentType) {
        return "text/csv".equals(contentType);
    }
}
