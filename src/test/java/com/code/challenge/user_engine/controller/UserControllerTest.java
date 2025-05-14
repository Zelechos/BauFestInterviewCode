package com.code.challenge.user_engine.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import com.code.challenge.user_engine.dto.SignUpRequest;
import com.code.challenge.user_engine.dto.UserResponse;
import com.code.challenge.user_engine.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@DisplayName("User Controller Tests")
class UserControllerTest {

    private MockMvc mockMvc;
    private SignUpRequest request;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        request = new SignUpRequest("alex", "exists@example.com", "passwoR34d", List.of());
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void signUp_MissingRequiredFields() throws Exception {
        mockMvc.perform(post("/api/v1/users/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUp_InvalidEmailFormat() throws Exception {

        mockMvc.perform(post("/api/v1/users/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_Success() throws Exception {
        String token = "valid.token.here";
        UserResponse response = UserResponse.builder()
                .email("user@example.com")
                .name("Test User")
                .build();

        when(userService.login(any(String.class))).thenReturn(response);

        mockMvc.perform(get("/api/v1/users/login")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@example.com"));
    }


    @Test
    void login_MissingAuthorizationHeader() throws Exception {
        mockMvc.perform(get("/api/v1/users/login"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUp_EmptyRequestBody() throws Exception {
        mockMvc.perform(post("/api/v1/users/sign-up")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUp_PasswordTooShort() throws Exception {
        SignUpRequest request = new SignUpRequest("alex", "test@example.com", "123", List.of());

        mockMvc.perform(post("/api/v1/users/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUp_NameTooLong() throws Exception {
        String longName = "a".repeat(256);
        SignUpRequest request = new SignUpRequest("aleoifbwoeufoweufoweufpowcefpewupfix", "test@example.com", "123", List.of());

        mockMvc.perform(post("/api/v1/users/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

}