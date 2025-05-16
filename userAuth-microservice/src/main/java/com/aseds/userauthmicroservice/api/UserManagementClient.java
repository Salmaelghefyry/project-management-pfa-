package com.aseds.userauthmicroservice.api;

import com.aseds.userauthmicroservice.model.AbstractUser;
import com.aseds.userauthmicroservice.model.RegisterRequest;
import com.aseds.userauthmicroservice.model.UserDTO;
import com.aseds.userauthmicroservice.model.UserDetail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service
public class UserManagementClient {

    private static final String USERS_ENDPOINT = "/users/{identifier}";
    private static final String REGISTER_ENDPOINT = "/register";
    private final PasswordEncoder passwordEncoder;
    private final String baseUrl;
    private final RestTemplate restTemplate;
    @Value("${jwt.internal}")
    private String internalSecretKey;
    public UserManagementClient(PasswordEncoder passwordEncoder, @Value("${user.management.api.base-url}") String baseUrl,
                                RestTemplate restTemplate) {
        this.passwordEncoder = passwordEncoder;
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    public Optional<UserDetails> getUserByIdentifier(String identifier) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-INTERNAL-SECRET", internalSecretKey);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<AbstractUser> response = restTemplate.exchange(
                    baseUrl + USERS_ENDPOINT,
                    HttpMethod.GET,
                    requestEntity,
                    AbstractUser.class,
                    identifier
            );

            return Optional.ofNullable(response.getBody())
                    .map(UserDetail::new);
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        } catch (RestClientException e) {
            throw new UserManagementException("Failed to retrieve user", e);
        }
    }

    public void registerUser(RegisterRequest registerRequest) {
        if (registerRequest == null) {
            throw new IllegalArgumentException("Register request cannot be null");
        }

        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-INTERNAL-SECRET", internalSecretKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<RegisterRequest> requestEntity = new HttpEntity<>(registerRequest, headers);

            restTemplate.exchange(
                    UriComponentsBuilder.fromUriString(baseUrl)
                            .path(REGISTER_ENDPOINT)
                            .build()
                            .toUriString(),
                    HttpMethod.POST,
                    requestEntity,
                    UserDTO.class
            );


        } catch (HttpClientErrorException.BadRequest e) {
            String responseMessage = e.getResponseBodyAsString();
            throw new IllegalArgumentException("Invalid registration data: " + responseMessage, e);

        } catch (HttpClientErrorException.Conflict e) {
            String responseMessage = e.getResponseBodyAsString();
            throw new IllegalArgumentException("User already exists: " + responseMessage, e);

        } catch (RestClientException e) {
            throw new UserManagementException("Failed to register user", e);
        }
    }

    public static class UserManagementException extends RuntimeException {
        public UserManagementException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}