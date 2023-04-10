package com.gds.challenge.service;

import com.gds.challenge.entity.User;
import com.gds.challenge.exceptions.CustomCsvValidationException;
import com.gds.challenge.exceptions.UploadFileException;
import com.gds.challenge.repository.CustomUsersRepository;
import com.gds.challenge.repository.UsersRepository;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    CustomUsersRepository customUsersRepository;

    @Mock
    UsersRepository usersRepository;

    @Mock
    MultipartFile file;

    @Test
    void getUsers() {
        List<User> EXPECTED_RESULT = new ArrayList<>();
        when(customUsersRepository.getUserResult(0f, 4000f, 0, Optional.empty(), Optional.empty()))
                .thenReturn(EXPECTED_RESULT);
        assertEquals(EXPECTED_RESULT,
                customUsersRepository.getUserResult(0f, 4000f, 0, Optional.empty(), Optional.empty()));
    }

    @Test
    void when_invalidHeaders_then_exception() throws IOException {
        String invalidHeader = "NAME,COLUMN";

        InputStream inputStream = new ByteArrayInputStream(invalidHeader.getBytes(StandardCharsets.UTF_8));
        when(file.getInputStream()).thenReturn(inputStream);

        CustomCsvValidationException exceptionThrown = assertThrows(
                CustomCsvValidationException.class,
                () -> userService.csvToUsers(file),
                "Should throw customCsvValidationException"
        );
        assertTrue(exceptionThrown.getMessage().contains("Invalid headers"));
        assertArrayEquals(new String[]{"NAME", "COLUMN"}, exceptionThrown.getLine());
        assertEquals(1L, exceptionThrown.getLineNumber());
    }

    @Test
    void when_invalidData_then_exception() throws IOException {
        String invalidHeader = "NAME,SALARY\nAlice,lorem Ipsum";

        InputStream inputStream = new ByteArrayInputStream(invalidHeader.getBytes(StandardCharsets.UTF_8));
        when(file.getInputStream()).thenReturn(inputStream);

        UploadFileException exceptionThrown = assertThrows(
                UploadFileException.class,
                () -> userService.csvToUsers(file),
                "Should throw customCsvValidationException"
        );
        assertTrue(exceptionThrown.getCause() instanceof CsvException);
        assertEquals(2L, ((CsvException) exceptionThrown.getCause()).getLineNumber());
        assertArrayEquals(((CsvException) exceptionThrown.getCause()).getLine(), new String[]{"Alice", "lorem Ipsum"});

    }

    @Test
    void when_file_valid_then_repo_called() throws IOException, CsvValidationException {
        String validFile = "NAME,SALARY\nTestUser,10.0";

        User testUser = User.builder().name("TestUser").salary(10.0f).build();

        InputStream inputStream = new ByteArrayInputStream(validFile.getBytes(StandardCharsets.UTF_8));
        when(file.getInputStream()).thenReturn(inputStream);
        userService.csvToUsers(file);

        verify(usersRepository, times(1)).save(testUser);
    }

    @Test
    void when_file_user_negative_salary_then_repo_not_saved() throws IOException, CsvValidationException {
        String validFile = "NAME,SALARY\nTestUser,-10.0";

        InputStream inputStream = new ByteArrayInputStream(validFile.getBytes(StandardCharsets.UTF_8));
        when(file.getInputStream()).thenReturn(inputStream);
        userService.csvToUsers(file);

        verify(usersRepository, never()).save(any(User.class));
    }
}