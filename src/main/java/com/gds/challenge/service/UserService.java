package com.gds.challenge.service;

import com.gds.challenge.exceptions.BusinessException;
import com.gds.challenge.entity.User;
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

    public List<User> csvToUsers(MultipartFile file) throws IOException, CsvValidationException {

        // TODO: validate that its not blank????

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVReader csvReader = new CSVReader(reader)) {

            // validate headers
            String[] header = csvReader.readNext();
            if (!isValidHeaders(header, CsvHeaders.getValues())) {
                throw new BusinessException("Invalid headers: Expected " + Arrays.toString(CsvHeaders.getValues()) +
                        " but found " + Arrays.toString(header));
            }
            reader.reset();

            // create csv bean reader
            CsvToBean csvToBean = new CsvToBeanBuilder(reader)
                    .withType(User.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .build();

            processUsers(csvToBean.iterator());


            return new ArrayList<>();
        }
    }

    @Transactional
    private void processUsers(Iterator<User> userIterator) {
        while (userIterator.hasNext()) {
            User user = userIterator.next();
            if (user.getSalary() >= 0.0f) { // filter here to let csv annotations do the grunt work
                System.out.println("inserting : \t" + user);
                // TODO: may want to use entityManager or batch updates???
                usersRepository.save(user);
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
