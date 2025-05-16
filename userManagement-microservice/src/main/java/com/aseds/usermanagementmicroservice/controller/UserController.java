package com.aseds.usermanagementmicroservice.controller;

import com.aseds.usermanagementmicroservice.model.AbstractUser;
import com.aseds.usermanagementmicroservice.model.dto.RegisterRequest;
import com.aseds.usermanagementmicroservice.model.dto.UserDTO;
import com.aseds.usermanagementmicroservice.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-management/auth")
public class UserController {
    private final AuthService authService;

    @Autowired
    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody RegisterRequest registerRequest) {
        UserDTO createdUser = authService.registerUser(registerRequest);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("users/{identifier}")
    public ResponseEntity<AbstractUser> getUserByIdentifier(@PathVariable String identifier) {
        AbstractUser user = authService.getUserByIdentifier(identifier);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


}
