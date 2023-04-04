package com.gds.challenge.controllers;


import com.gds.challenge.model.FileUploadStatus;
import com.gds.challenge.model.UserQueryResult;
import com.gds.challenge.service.UserService;
import com.gds.challenge.utils.UserSortType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public FileUploadStatus uploadFile() {
        return FileUploadStatus.SUCCESS;
    }

}
