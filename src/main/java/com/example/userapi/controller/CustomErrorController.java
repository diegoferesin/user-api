package com.example.userapi.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * Custom error controller to handle error pages.
 */
@Controller
public class CustomErrorController implements ErrorController {

    /**
     * Handles all error requests and returns appropriate error pages.
     *
     * @param request the HTTP request
     * @return the name of the error view template
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error-404";
            }
        }
        return "error";
    }

    /**
     * Returns the error path.
     *
     * @return the error path
     */
    public String getErrorPath() {
        return "/error";
    }
} 