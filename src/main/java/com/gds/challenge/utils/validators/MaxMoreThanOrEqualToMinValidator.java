package com.gds.challenge.utils.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;

/**
 * Validate parameters on {@UserController} getUser
 * ensure max salary is greater than min salary
 */
@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class MaxMoreThanOrEqualToMinValidator
        implements ConstraintValidator<MaxMoreThanOrEqualToMin, Object[]> {

    @Override
    public boolean isValid(
            Object[] value,
            ConstraintValidatorContext context) {
        float min = (Float) value[0];
        float max = (Float) value[1];
        return max >= min;
    }
}