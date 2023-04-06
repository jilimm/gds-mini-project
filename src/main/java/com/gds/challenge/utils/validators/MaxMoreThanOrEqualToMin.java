package com.gds.challenge.utils.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = MaxMoreThanOrEqualToMinValidator.class)
@Target({ METHOD, CONSTRUCTOR })
@Retention(RUNTIME)
@Documented
public @interface MaxMoreThanOrEqualToMin {

    String message() default
            "`max` must be more than or equal to `min`";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
