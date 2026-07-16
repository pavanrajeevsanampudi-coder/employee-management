package com.example.employee_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.employee_management.dto.UserDTO;
import com.example.employee_management.dto.UserResponseDTO;
import com.example.employee_management.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public UserResponseDTO register(@Valid @RequestBody UserDTO userDto) {
        return userService.registerUser(userDto);
    }
}
