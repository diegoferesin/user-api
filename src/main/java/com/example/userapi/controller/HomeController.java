package com.example.userapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for handling the home page and root path.
 */
@Controller
public class HomeController {

    /**
     * Handles the root path request.
     *
     * @return the name of the home view template
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/swagger-ui.html";
    }
} 