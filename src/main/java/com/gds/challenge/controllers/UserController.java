package com.gds.challenge.controllers;


import com.gds.challenge.BusinessException;
import com.gds.challenge.entity.User;
import com.gds.challenge.model.FileUploadStatus;
import com.gds.challenge.model.UserQueryResult;
import com.gds.challenge.service.UserService;
import com.gds.challenge.utils.UserSortType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeType;
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
    public FileUploadStatus uploadFile(@RequestParam("file") MultipartFile file) {
        // https://www.pixeltrice.com/import-the-csv-file-into-mysql-database-using-spring-boot-application/
        // https://www.bezkoder.com/spring-boot-upload-csv-file/

        // https://stackoverflow.com/questions/25460779/converting-validating-csv-file-upload-in-spring-mvc
        // https://stackoverflow.com/questions/44171737/how-to-read-validate-and-move-a-csv-file

        // https://gist.github.com/susimsek/03b6a4d695b864dfe95d1b31959b3e53
        // TODO: validate file content type in service
        if (!"text/csv".equals(file.getContentType())) {
            // TODO: error handling for business exception
            throw new BusinessException("incorrect file format" + file.getContentType());
        }


        try {
            List<User> userList = userService.csvToUsers(file);
            System.out.println(Arrays.toString(userList.toArray()));
        } catch (IOException e) {
            return FileUploadStatus.FAILURE;
        }
        return FileUploadStatus.SUCCESS;


    }

}
