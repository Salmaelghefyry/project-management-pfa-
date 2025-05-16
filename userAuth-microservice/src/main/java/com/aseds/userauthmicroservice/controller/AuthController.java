package com.aseds.userauthmicroservice.controller;

import com.aseds.userauthmicroservice.enums.Roles;
import com.aseds.userauthmicroservice.model.AbstractUser;
import com.aseds.userauthmicroservice.model.LoginRequest;
import com.aseds.userauthmicroservice.model.RegisterRequest;
import com.aseds.userauthmicroservice.model.UserDetail;
import com.aseds.userauthmicroservice.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest userDetail) {
        try {
            AbstractUser user=new AbstractUser();
            user.setEmail(userDetail.getEmail());
            user.setUsername(userDetail.getUsername());
            user.setPassword(userDetail.getPassword());
            user.setPrenom(userDetail.getPrenom());
            user.setNom(userDetail.getNom());
            user.setRole(userDetail.getRole());
            UserDetail newUser=new UserDetail(user);
            authService.createUser(newUser);
            return ResponseEntity.ok("User registered successfully");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest){
        try {
            String token=authService.login(loginRequest);
            return ResponseEntity.ok(token);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("exist/{identifier}")
    public ResponseEntity<Boolean> isExistUser(@PathVariable String identifier){
        return ResponseEntity.ok(authService.userExists(identifier));
    }
}
