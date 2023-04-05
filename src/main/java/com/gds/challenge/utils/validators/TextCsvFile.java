package com.gds.challenge.utils.validators;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {TextCsvFileValidator.class})
public @interface TextCsvFile {
    String message() default "Only non empty text/csv files are allowed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}