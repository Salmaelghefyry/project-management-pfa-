package com.aseds.userauthmicroservice.services;

import com.aseds.userauthmicroservice.api.UserManagementClient;
import com.aseds.userauthmicroservice.model.LoginRequest;
import com.aseds.userauthmicroservice.model.RegisterRequest;
import com.aseds.userauthmicroservice.model.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
public class AuthService implements UserDetailsManager {
    private final UserManagementClient userManagementClient;
    private final UserDetailService userDetailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public AuthService(UserManagementClient userManagementClient, UserDetailService userDetailService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userManagementClient = userManagementClient;
        this.userDetailService = userDetailService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public void createUser(UserDetails user) {
        try {
            RegisterRequest newUser = new RegisterRequest();
            newUser.setEmail(((UserDetail) user).getEmail());
            newUser.setPassword(user.getPassword());
            newUser.setNom(((UserDetail) user).getNom());
            newUser.setPrenom(((UserDetail) user).getPrenom());
            newUser.setUsername(user.getUsername());
            newUser.setRole(((UserDetail) user).getRole());
            userManagementClient.registerUser(newUser);
        }catch (RestClientException e) {
            throw new RestClientException("Failed to register user", e);
        }
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return loadUserByUsername(username) != null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDetailService.loadUserByUsername(username);
    }

    public String login(LoginRequest loginRequest) {
        if (loginRequest.getIdentifier() == null || loginRequest.getIdentifier().isBlank()) {
            throw new IllegalArgumentException("Identifier cannot be null or empty");
        }

        if (loginRequest.getPassword() == null || loginRequest.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        try {
            Authentication authentication = this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getIdentifier(),
                            loginRequest.getPassword()
                    )
            );

            UserDetail userDetails = (UserDetail) authentication.getPrincipal();
            return this.jwtService.generateToken(userDetails.user());

        } catch (AuthenticationException e) {
            throw new org.springframework.security.authentication.BadCredentialsException("Invalid credentials");
        }
    }
}
