package com.gds.challenge.controllers;


import com.gds.challenge.entity.User;
import com.gds.challenge.model.FileUploadStatus;
import com.gds.challenge.model.UserQueryResult;
import com.gds.challenge.service.UserService;
import com.gds.challenge.utils.UserSortType;
import com.gds.challenge.utils.validators.TextCsvFile;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public UserQueryResult getUser(@RequestParam(defaultValue = "0.0") @Positive float min,
                                   @RequestParam(defaultValue = "4000.0") @Positive float max,
                                   @RequestParam(defaultValue = "0") @Positive int offset,
                                   @RequestParam(required = false) @Positive Optional<Integer> limit,
                                   @RequestParam(required = false) @NotBlank Optional<UserSortType> sort) {
        return UserQueryResult.builder()
                .results(userService.getUsers(min, max, offset, limit, sort))
                .build();
    }

    @PostMapping("/upload")
    public FileUploadStatus uploadFile(@RequestParam("file") @Validated @TextCsvFile MultipartFile file) {
        try {
            List<User> userList = userService.csvToUsers(file);
            System.out.println(Arrays.toString(userList.toArray()));
        } catch (IOException | CsvValidationException e) {
            // TODO: catch in exception handler???
            return FileUploadStatus.FAILURE;
        }
        return FileUploadStatus.SUCCESS;


    }

}
