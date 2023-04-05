package com.gds.challenge.service;

import com.gds.challenge.entity.User;
import com.gds.challenge.entity.repository.CustomUsersRepository;
import com.gds.challenge.utils.UserSortType;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
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

        // https://www.baeldung.com/apache-commons-csv

        // TODO: use open csv for validation. hopefully remove all the if throw stuff
        // https://medium.com/javarevisited/how-to-read-csv-file-using-open-csv-in-java-6796db168870


        // TODO: validate headers
        // TODO: validate the number of ","
        // TODO: validate that its not blank????
        // TODO: storing all users here may not be good idea if file is super big??????
        // TODO: check if salary cannot be parsed catch NumberFormatException

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            // https://attacomsian.com/blog/spring-boot-upload-parse-csv-file

            // create csv bean reader
            // TODO .withVerifyReader
            CsvToBean<User> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(User.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator(',')
                    .withFilter(line -> {
                        System.out.println(Arrays.toString(line));
                        return Float.parseFloat(line[1]) > 0;
                    })
                    .build();

            // TODO: if file is really huge its not a good idea to store it in memory
            return csvToBean.parse();

        }

    }


}
