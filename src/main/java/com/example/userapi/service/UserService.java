package com.example.userapi.service;

import com.example.userapi.dto.UserRegistrationRequest;
import com.example.userapi.dto.UserResponse;
import com.example.userapi.exception.EmailAlreadyExistsException;
import com.example.userapi.exception.ValidationException;

public interface UserService {
    /**
     * Register a new user with the provided details
     * 
     * @param request The user registration data
     * @return UserResponse with the created user details
     * @throws EmailAlreadyExistsException if the email is already registered
     * @throws ValidationException if validation fails
     */
    UserResponse registerUser(UserRegistrationRequest request) throws EmailAlreadyExistsException, ValidationException;
}