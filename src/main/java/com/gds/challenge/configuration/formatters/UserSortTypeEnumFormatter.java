package com.gds.challenge.configuration.formatters;

import com.gds.challenge.exceptions.BusinessException;
import com.gds.challenge.utils.UserSortType;
import jakarta.validation.ConstraintViolationException;
import org.springframework.format.Formatter;
import org.springframework.http.HttpStatus;

import java.util.Locale;

/**
 * allow UserSortType to be NON-case sensitive
 */
public class UserSortTypeEnumFormatter implements Formatter<UserSortType> {

    @Override
    public UserSortType parse(String text, Locale locale) {

        try {
            return UserSortType.valueOf(text.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("here~~~");
            return null;
        }

    }

    @Override
    public String print(UserSortType object, Locale locale) {
        return object.toString();
    }
}
