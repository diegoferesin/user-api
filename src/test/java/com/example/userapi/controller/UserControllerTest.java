package com.example.userapi.controller;

import com.example.userapi.controller.impl.UserControllerImpl;
import com.example.userapi.dto.UserRegistrationRequest;
import com.example.userapi.dto.UserResponse;
import com.example.userapi.exception.EmailAlreadyExistsException;
import com.example.userapi.exception.GlobalExceptionHandler;
import com.example.userapi.exception.ValidationException;
import com.example.userapi.model.Phone;
import com.example.userapi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserControllerImpl userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UserRegistrationRequest validRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        List<Phone> phones = new ArrayList<>();
        phones.add(Phone.builder()
                .number("1234567")
                .citycode("1")
                .countrycode("57")
                .build());

        validRequest = new UserRegistrationRequest();
        validRequest.setName("Juan Rodriguez");
        validRequest.setEmail("juan@rodriguez.org");
        validRequest.setPassword("Password1@");
        validRequest.setPhones(phones);

        UUID userId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        userResponse = UserResponse.builder()
                .id(userId)
                .name(validRequest.getName())
                .email(validRequest.getEmail())
                .created(now)
                .modified(now)
                .lastLogin(now)
                .token("jwt.token.here")
                .isActive(true)
                .build();
    }

    @Test
    void registerUser_ValidRequest_ReturnsCreatedUser() throws Exception {
        // Arrange
        when(userService.registerUser(any(UserRegistrationRequest.class))).thenReturn(userResponse);
        
        // Act & Assert
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(validRequest.getName()))
                .andExpect(jsonPath("$.email").value(validRequest.getEmail()))
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.is_active").value(true));
    }

    @Test
    void registerUser_EmailAlreadyExists_ReturnsConflict() throws Exception {
        // Arrange
        when(userService.registerUser(any(UserRegistrationRequest.class)))
                .thenThrow(new EmailAlreadyExistsException("the email is already registered"));
        
        // Act & Assert
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("the email is already registered"));
    }

    @Test
    void registerUser_ValidationFails_ReturnsBadRequest() throws Exception {
        // Arrange
        when(userService.registerUser(any(UserRegistrationRequest.class)))
                .thenThrow(new ValidationException("El formato del correo electrónico es inválido"));
        
        // Act & Assert
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El formato del correo electrónico es inválido"));
    }
}