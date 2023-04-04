package com.gds.challenge.configuration;

import com.fasterxml.jackson.databind.util.Converter;
import com.gds.challenge.configuration.formatters.UserSortTypeEnumFormatter;
import com.gds.challenge.utils.UserSortType;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class EnumMappingConfigurations implements WebMvcConfigurer {

    // https://devwithus.com/enum-mapping-spring-boot/
    UserSortTypeEnumFormatter userSortTypeEnumFormatter() {
        return new UserSortTypeEnumFormatter();
    }
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(userSortTypeEnumFormatter());
    }


}
