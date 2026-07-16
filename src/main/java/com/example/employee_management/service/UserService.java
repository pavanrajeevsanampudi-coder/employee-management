package com.example.employee_management.service;

import com.example.employee_management.model.User;
import com.example.employee_management.dto.UserDTO;
import com.example.employee_management.dto.UserResponseDTO;
import com.example.employee_management.repository.UserRepository;
import com.example.employee_management.exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Register user using DTO
    public UserResponseDTO registerUser(UserDTO dto) {
        logger.info("Attempting to register user with email: {}", dto.getEmail());

        // Check if email already exists
        if (userRepository.findByEmail(dto.getEmail()) != null) {
            logger.error("Registration failed - email already exists: {}", dto.getEmail());
            throw new UserAlreadyExistsException("Email already exists");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Normalize role
        if (dto.getRole() == null || dto.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        } else if (!dto.getRole().startsWith("ROLE_")) {
            user.setRole("ROLE_" + dto.getRole().toUpperCase());
        } else {
            user.setRole(dto.getRole());
        }

        User savedUser = userRepository.save(user);
        logger.info("User registered successfully with id: {}", savedUser.getId());

        // Convert to Response DTO (hide password)
        UserResponseDTO response = new UserResponseDTO();
        response.setId(savedUser.getId());
        response.setUsername(savedUser.getUsername());
        response.setEmail(savedUser.getEmail());
        response.setRole(savedUser.getRole());

        return response;
    }

    public User findByEmail(String email) {
        logger.info("Searching for user with email: {}", email);
        return userRepository.findByEmail(email);
    }
}
