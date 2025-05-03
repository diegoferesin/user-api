package com.example.userapi.service.impl;

import com.example.userapi.dto.UserRegistrationRequest;
import com.example.userapi.dto.UserResponse;
import com.example.userapi.exception.EmailAlreadyExistsException;
import com.example.userapi.exception.ValidationException;
import com.example.userapi.model.User;
import com.example.userapi.repository.UserRepository;
import com.example.userapi.service.JwtService;
import com.example.userapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${app.validation.email.regex}")
    private String emailRegex;

    @Value("${app.validation.password.regex}")
    private String passwordRegex;

    @Override
    public UserResponse registerUser(UserRegistrationRequest request) {
        log.info("Processing registration request for email: {}", request.getEmail());

        // Validate email format
        if (!Pattern.matches(emailRegex, request.getEmail())) {
            log.error("Email validation failed for: {}", request.getEmail());
            throw new ValidationException("invalid email address format");
        }

        // Validate password format
        if (!Pattern.matches(passwordRegex, request.getPassword())) {
            log.error("Password validation failed");
            throw new ValidationException("invalid password format");
        }

        // Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            log.error("Email already exists: {}", request.getEmail());
            throw new EmailAlreadyExistsException("email already exists");
        }

        // Create user
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phones(request.getPhones())
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .isActive(true)
                .build();

        // Generate token
        String token = jwtService.generateToken(user);
        user.setToken(token);

        // Save user to the database
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());


        // Build response
        return UserResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .created(savedUser.getCreated())
                .modified(savedUser.getModified())
                .lastLogin(savedUser.getLastLogin())
                .token(savedUser.getToken())
                .isActive(savedUser.isActive())
                .build();
    }
}