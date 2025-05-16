package com.aseds.usermanagementmicroservice.services;

import com.aseds.usermanagementmicroservice.mappers.implementation.UserMapperDTO;
import com.aseds.usermanagementmicroservice.model.AbstractUser;
import com.aseds.usermanagementmicroservice.model.ProjectManagerEntity;
import com.aseds.usermanagementmicroservice.model.dto.UserDTO;
import com.aseds.usermanagementmicroservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapperDTO userMapperDTO;

    @Autowired
    public UserService(UserRepository userRepository, UserMapperDTO userMapperDTO) {
        this.userRepository = userRepository;
        this.userMapperDTO = userMapperDTO;
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public UserDTO partialUpdateUserById(Long id, AbstractUser updatedUser) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (updatedUser == null) {
            throw new IllegalArgumentException("Updated user data cannot be null");
        }

        updatedUser.setId(id);

        return userRepository.findById(id)
                .map(existingUser -> updateUserFields(existingUser, updatedUser))
                .orElseThrow(() -> createUserNotFoundException(id));
    }

    private UserDTO updateUserFields(AbstractUser existingUser, AbstractUser updatedUser) {
        Optional.ofNullable(updatedUser.getEmail())
                .filter(email -> !email.equals(existingUser.getEmail()))
                .ifPresent(email -> {
                    validateEmailUniqueness(email, existingUser.getId());
                    existingUser.setEmail(email);
                });


        Optional.ofNullable(updatedUser.getUsername()).ifPresent(existingUser::setUsername);
        Optional.ofNullable(updatedUser.getPassword()).ifPresent(existingUser::setPassword);

        return userMapperDTO.mapTo(userRepository.save(existingUser));
    }

    private void validateEmailUniqueness(String email, Long currentUserId) {
        userRepository.findUserByEmail(email)
                .ifPresent(user -> {
                    if (!user.getId().equals(currentUserId)) {
                        throw new IllegalArgumentException("Email already in use: " + email);
                    }
                });
    }



    private RuntimeException createUserNotFoundException(Long id) {
        return new IllegalArgumentException("User not found with ID: " + id);
    }

    public Page<UserDTO> findAll(Pageable pageable) {
        if (pageable == null) {
            throw new IllegalArgumentException("Pageable parameter cannot be null");
        }
        try {
            Page<AbstractUser> userPage = userRepository.findAll(pageable);
            return userPage.map(userMapperDTO::mapTo);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving users", e);
        }
    }

    public UserDTO findUserById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return userRepository.findById(id)
                .map(userMapperDTO::mapTo)
                .orElseThrow(() -> createUserNotFoundException(id));
    }

    public boolean isUserExists(Long id) {
        return userRepository.existsById(id);
    }
}
