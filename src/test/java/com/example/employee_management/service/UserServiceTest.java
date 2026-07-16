package com.example.employee_management.service;

import com.example.employee_management.dto.UserDTO;
import com.example.employee_management.dto.UserResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void testRegisterUser() {
        UserDTO dto = new UserDTO();
        dto.setUsername("testuser");
        dto.setEmail("testuser@example.com");
        dto.setPassword("password123");
        dto.setRole("USER");

        UserResponseDTO saved = userService.registerUser(dto);

        assertNotNull(saved.getId(), "User ID should not be null after registration");
        assertEquals("testuser", saved.getUsername());
    }
}
