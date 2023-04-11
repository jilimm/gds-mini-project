package com.gds.challenge.controllers;


import com.gds.challenge.exceptions.UploadFileException;
import com.gds.challenge.model.GenericResponse;
import com.gds.challenge.model.UserQueryResult;
import com.gds.challenge.service.UserService;
import com.gds.challenge.utils.UserSortType;
import com.gds.challenge.utils.validators.MaxMoreThanOrEqualToMin;
import com.gds.challenge.utils.validators.TextCsvFile;
import com.opencsv.exceptions.CsvException;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

/**
 * REST Controller for users
 */
@RestController
@Validated
public class UserController {

    @Autowired
    UserService userService;

    /**
     * Search list of users with parameters
     *
     * @param min    minimum Salary
     * @param max    maximum Salary
     * @param offset offset for result
     * @param limit  limit for result
     * @param sort   sorting type, not case-sensitive
     * @return result list of users
     */
    @GetMapping(value = "/users", produces = "application/json")
    @MaxMoreThanOrEqualToMin
    public UserQueryResult getUser(@RequestParam(defaultValue = "0.0") @PositiveOrZero float min, @RequestParam(defaultValue = "4000.0") @PositiveOrZero float max, @RequestParam(defaultValue = "0") @PositiveOrZero int offset, @RequestParam(required = false) Optional<@PositiveOrZero Integer> limit, @RequestParam(required = false) Optional<UserSortType> sort) {
        return UserQueryResult.builder().results(userService.getUsers(min, max, offset, limit, sort)).build();
    }

    /**
     * Update users database with data from csv file
     *
     * @param file csv file
     * @return success code.
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "application/json")
    public GenericResponse uploadFile(@RequestParam("file") @Validated @TextCsvFile MultipartFile file) {
        try {
            userService.csvToUsers(file);
            return GenericResponse.builder().success(1).build();
        } catch (IOException | CsvException e) {
            throw new UploadFileException("Error encountered when uploading file", e);
        }

    }

}
