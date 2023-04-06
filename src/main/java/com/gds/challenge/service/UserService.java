package com.gds.challenge.service;

import com.gds.challenge.exceptions.BusinessException;
import com.gds.challenge.entity.User;
import com.gds.challenge.exceptions.CustomCsvValidationException;
import com.gds.challenge.repository.CustomUsersRepository;
import com.gds.challenge.repository.UsersRepository;
import com.gds.challenge.utils.UserSortType;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.transaction.Transactional;
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
import java.util.*;


@Service
@Transactional
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    CustomUsersRepository customUsersRepository;

    @Autowired
    UsersRepository usersRepository;


    public List<User> getUsers(float minSalary,
                               float maxSalary,
                               int offset,
                               Optional<Integer> limit,
                               Optional<UserSortType> sortType) {
        logger.info("min: " + minSalary + " max: " + maxSalary + " offset: " + offset + " limit: " + limit + " sortType: " + sortType);
        return customUsersRepository.getUserResult(minSalary, maxSalary, offset, limit, sortType);

    }

    public void csvToUsers(MultipartFile file) throws IOException, CsvValidationException {

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVReader csvReader = new CSVReader(reader)) {

            // validate headers
            String[] header = csvReader.readNext();
            System.out.println(Arrays.toString(header));
            if (!isValidHeaders(header, CsvHeaders.getValues())) {
                CustomCsvValidationException customCsvValidationException = new CustomCsvValidationException(
                        "Invalid headers detected. Headers should be "+Arrays.toString(CsvHeaders.getValues()));
                customCsvValidationException.setLine(header);
                customCsvValidationException.setLineNumber(1L);
                throw customCsvValidationException;
            }
            reader.reset();

            // create csv bean reader
            CsvToBean csvToBean = new CsvToBeanBuilder(reader)
                    .withType(User.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .build();

            processUsers(csvToBean.iterator());

        }
    }

    @Transactional
    private void processUsers(Iterator<User> userIterator) {
        while (userIterator.hasNext()) {
            User user = userIterator.next();
            if (user.getSalary() >= 0.0f) { // filter here to let csv annotations do the grunt work
                // TODO: may want to use entityManager or batch updates???
                // investigate: https://medium.com/geekculture/spring-transactional-rollback-handling-741fcad043c6
                // https://reflectoring.io/spring-transactions-and-exceptions/
                usersRepository.save(user);
            } else {
                logger.debug("Ignoring user: "+user.getName());
            }
        }
    }

    private boolean isValidHeaders(String[] headers, String[] expectedHeaders) {
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
