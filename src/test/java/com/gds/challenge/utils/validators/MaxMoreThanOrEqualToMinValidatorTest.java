package com.gds.challenge.utils.validators;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
class MaxMoreThanOrEqualToMinValidatorTest {

    @InjectMocks
    MaxMoreThanOrEqualToMinValidator maxMoreThanOrEqualToMinValidator;

    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    @Test
    void when_max_equal_to_min_then_valid() {
        Object[] input = new Object[]{1.0f, 1.0f};
        assertTrue(maxMoreThanOrEqualToMinValidator.isValid(input, constraintValidatorContext));
    }

    void when_max_moreThan_min_then_valid() {
        Object[] input = new Object[]{1.0f, 5.0f};
        assertTrue(maxMoreThanOrEqualToMinValidator.isValid(input, constraintValidatorContext));
    }

    @Test
    void when_max_lessThan_min_then_valid() {
        Object[] input = new Object[]{10.0f, 2.0f};
        assertFalse(maxMoreThanOrEqualToMinValidator.isValid(input, constraintValidatorContext));
    }
}