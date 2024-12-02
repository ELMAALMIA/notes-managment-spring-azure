package com.dev.Task_management.controllers;
import com.dev.Task_management.entities.User;
import com.dev.Task_management.services.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@Controller
public class AuthController {
    private final UserServiceImpl userService;


    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("user") User user,
                         @RequestParam("confirmPassword") String confirmPassword,
                         Model model) {
        // Check if passwords match
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match. Please try again.");
            return "signup"; // Return to the signup page with an error message
        }

        // Check if the username is already taken
        if (userService.isUsernameTaken(user.getUsername())) {
            model.addAttribute("error", "Username is already taken. Please choose a different username.");
            return "signup"; // Return to the signup page with an error message
        }

        // Encode the password before saving the user
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);

        // Save the user to the database
        userService.saveUser(user);

        // Redirect to the login page after successful signup
        return "redirect:/signin";
    }

    @GetMapping("/signup")
    public String register(Model m)
    {
        m.addAttribute("title","REGISTER");
        return "signup";
    }

    @GetMapping("/signin")
    public String showLoginForm() {
        return "login"; // This will return the login.html Thymeleaf template
    }
}