package com.gds.challenge.configuration.formatters;

import com.gds.challenge.utils.UserSortType;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.Formatter;

import java.util.Locale;

/**
 * allow UserSortType to be NON-case sensitive
 */
public class UserSortTypeEnumFormatter implements Formatter<UserSortType> {

    @Override
    public UserSortType parse(String text, Locale locale) {
        if (StringUtils.isBlank(text)) {
            return null;
        }

        return EnumUtils.getEnum(UserSortType.class, text.toUpperCase());
    }

    @Override
    public String print(UserSortType object, Locale locale) {
        return object.toString();
    }
}
