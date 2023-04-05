package com.gds.challenge.service;

import com.gds.challenge.BusinessException;
import com.gds.challenge.entity.User;
import com.gds.challenge.entity.repository.CustomUsersRepository;
import com.gds.challenge.utils.UserSortType;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvValidationException;
import com.opencsv.validators.RowFunctionValidator;
import com.opencsv.validators.RowValidator;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.boot.jaxb.internal.MappingBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserService {

    public static final String HEADER_NAME = "NAME";
    public static final String HEADER_SALARY = "SALARY";
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    CustomUsersRepository customUsersRepository;

    public List<User> getUsers(float minSalary,
                               float maxSalary,
                               int offset,
                               Optional<Integer> limit,
                               Optional<UserSortType> sortType) {
        logger.info("min: " + minSalary + " max: " + maxSalary + " offset: " + offset + " limit: " + limit + " sortType: " + sortType);
        return customUsersRepository.getUserResult(minSalary, maxSalary, offset, limit, sortType);

    }

    public List<User> csvToUsers(MultipartFile file) throws IOException {

        // TODO: validate that its not blank????
        // TODO: storing all users here may not be good idea if file is super big??????
        // TODO: check if salary cannot be parsed catch NumberFormatException

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            // validate headers
            CSVReader csvReader1 = new CSVReader(reader);
            String[] header = csvReader1.readNext();
            if (!isValidHeaders(header, new String[]{HEADER_NAME, HEADER_SALARY})) {
                throw new BusinessException("Invalid headers: "+ Arrays.toString(header)+ "only expected headers NAME and SALARY");
            }
            reader.reset();

            // create csv bean reader
            CsvToBean csvToBean = new CsvToBeanBuilder(reader)
                    .withType(User.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .withFilter(line -> {
                        System.out.println(Arrays.toString(line));
                        return Float.parseFloat(line[1]) > 0;
                    })
                    .build();


            // TODO: if file is really huge its not a good idea to store it in memory
            return csvToBean.parse();

        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

    }

    private boolean isValidHeaders(String[] headers, String[] expectedHeaders ) {
        return Arrays.deepEquals(headers, expectedHeaders);
    }

    enum CsvHeaders {
        NAME,
        SALARY;

        public static String[] getValues() {
            return Arrays.stream(CsvHeaders.values())
                            .map(CsvHeaders::toString)
                    .toArray(String[]::new);
        }


    }

}
