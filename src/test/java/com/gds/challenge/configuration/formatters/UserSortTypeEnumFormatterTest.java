package com.gds.challenge.configuration.formatters;

import com.gds.challenge.utils.UserSortType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
class UserSortTypeEnumFormatterTest {

    @InjectMocks
    UserSortTypeEnumFormatter userSortTypeEnumFormatter;

    @Test
    void when_parse_upper_case_then_enum_returned() {
        assertEquals(UserSortType.NAME, userSortTypeEnumFormatter.parse(UserSortType.NAME.name(), Locale.ENGLISH));
    }

    @Test
    void when_parse_mixed_case_then_enum_returned() {
        assertEquals(UserSortType.NAME, userSortTypeEnumFormatter.parse("nAmE", Locale.ENGLISH));
    }

    @Test
    void when_parse_invalid_then_null_returned() {
        assertNull(userSortTypeEnumFormatter.parse("lorem ipsum", Locale.ENGLISH));
    }
}