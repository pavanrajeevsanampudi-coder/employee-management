package com.example.employee_management.controller;

import com.example.employee_management.model.User;
import com.example.employee_management.service.UserService;
import com.example.employee_management.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        User user = userService.findByEmail(email); // fetch user from DB
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // generate token with username
        	return jwtUtil.generateToken(user.getUsername(), user.getRole());

        } else {
            return "Invalid credentials";
        }
    }
}

