package com.example.userapi.service;

import com.example.userapi.dto.UserRegistrationRequest;
import com.example.userapi.dto.UserResponse;
import com.example.userapi.exception.EmailAlreadyExistsException;
import com.example.userapi.exception.ValidationException;
import com.example.userapi.model.Phone;
import com.example.userapi.model.User;
import com.example.userapi.repository.UserRepository;
import com.example.userapi.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegistrationRequest validRequest;
    private User validUser;
    private final String validToken = "valid.jwt.token";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userService, "emailRegex", "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        ReflectionTestUtils.setField(userService, "passwordRegex", "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");

        Phone phone = Phone.builder()
                .number("123456789")
                .citycode("1")
                .countrycode("57")
                .build();

        validRequest = new UserRegistrationRequest();
        validRequest.setName("Juan Rodriguez");
        validRequest.setEmail("juan@rodriguez.org");
        validRequest.setPassword("Hunter2@");
        validRequest.setPhones(Collections.singletonList(phone));

        validUser = User.builder()
                .id(UUID.randomUUID())
                .name(validRequest.getName())
                .email(validRequest.getEmail())
                .password("encodedPassword")
                .phones(validRequest.getPhones())
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .token(validToken)
                .isActive(true)
                .build();
    }

    @Test
    void registerUser_ValidRequest_ReturnsUserResponse() {
        when(userRepository.findByEmail(validRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(validUser);
        when(jwtService.generateToken(any(User.class))).thenReturn(validToken);

        UserResponse response = userService.registerUser(validRequest);

        assertNotNull(response);
        assertEquals(validUser.getId(), response.getId());
        assertEquals(validUser.getName(), response.getName());
        assertEquals(validUser.getEmail(), response.getEmail());
        assertEquals(validToken, response.getToken());
        assertTrue(response.isActive());
    }

    @Test
    void registerUser_EmailAlreadyExists_ThrowsException() {
        when(userRepository.findByEmail(validRequest.getEmail())).thenReturn(Optional.of(validUser));

        assertThrows(EmailAlreadyExistsException.class, () -> {
            userService.registerUser(validRequest);
        });
    }

    @Test
    void registerUser_InvalidEmail_ThrowsException() {
        validRequest.setEmail("invalid-email");

        assertThrows(ValidationException.class, () -> {
            userService.registerUser(validRequest);
        });
    }

    @Test
    void registerUser_InvalidPassword_ThrowsException() {
        validRequest.setPassword("weak");

        assertThrows(ValidationException.class, () -> {
            userService.registerUser(validRequest);
        });
    }
}