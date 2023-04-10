package com.gds.challenge.controllers;

import com.gds.challenge.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @BeforeEach
    void setUp(){
        when(userService.getUsers(anyInt(), anyInt(), anyInt(), any(), any()))
                .thenReturn(new ArrayList<>());
    }

    @Test
    void getUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.results").exists());

        verify(userService, times(1))
                .getUsers(eq(0.0f), eq(4000.0f), eq(0),
                        eq(Optional.empty()), eq(Optional.empty()));
    }

    @Test
    void uploadFile() throws Exception {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                "text/csv",
                "Hello, World!".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/upload")
                        .file(file))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").exists());

        verify(userService, times(1))
                .csvToUsers(eq(file));


    }
}