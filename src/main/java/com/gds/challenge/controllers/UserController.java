package com.gds.challenge.controllers;


import com.gds.challenge.exceptions.UploadFileException;
import com.gds.challenge.model.GenericResponse;
import com.gds.challenge.model.UserQueryResult;
import com.gds.challenge.service.UserService;
import com.gds.challenge.utils.UserSortType;
import com.gds.challenge.utils.validators.MaxMoreThanOrEqualToMin;
import com.gds.challenge.utils.validators.TextCsvFile;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@Validated
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    @MaxMoreThanOrEqualToMin
    public UserQueryResult getUser(@RequestParam(defaultValue = "0.0") @PositiveOrZero float min,
                                   @RequestParam(defaultValue = "4000.0") @PositiveOrZero float max,
                                   @RequestParam(defaultValue = "0") @PositiveOrZero int offset,
                                   @RequestParam(required = false)  Optional<@PositiveOrZero  Integer> limit,
                                   @RequestParam(required = false)  Optional<UserSortType> sort) {

        return UserQueryResult.builder()
                .results(userService.getUsers(min, max, offset, limit, sort))
                .build();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public GenericResponse uploadFile(@RequestParam("file") @Validated @TextCsvFile MultipartFile file) {
        try {
            userService.csvToUsers(file);
            return GenericResponse.builder().success(1).build();
        } catch (IOException | CsvValidationException e) {
            throw new UploadFileException("error", e);
        }

    }

}
