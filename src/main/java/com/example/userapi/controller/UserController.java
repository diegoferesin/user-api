package com.example.userapi.controller;

import com.example.userapi.dto.UserRegistrationRequest;
import com.example.userapi.dto.UserResponse;
import com.example.userapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("/users")
@Tag(name = "User Management", description = "APIs for managing users")
public interface UserController {

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user with the given information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User registered successfully", 
            content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegistrationRequest request);
}