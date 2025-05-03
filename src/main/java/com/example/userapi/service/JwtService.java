package com.example.userapi.service;

import com.example.userapi.model.User;

/**
 * Service interface for JWT operations.
 */
public interface JwtService {
    /**
     * Generates a JWT token for the given user.
     *
     * @param user the user for whom to generate the token
     * @return the generated JWT token
     */
    String generateToken(User user);

    /**
     * Validates a JWT token.
     *
     * @param token the token to validate
     * @return true if the token is valid, false otherwise
     */
    boolean validateToken(String token);

    /**
     * Extracts the subject (email) from a JWT token.
     *
     * @param token the token from which to extract the subject
     * @return the subject (email) from the token
     */
    String getSubjectFromToken(String token);
}