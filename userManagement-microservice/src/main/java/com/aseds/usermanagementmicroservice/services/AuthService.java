package com.aseds.usermanagementmicroservice.services;

import com.aseds.usermanagementmicroservice.enums.Roles;
import com.aseds.usermanagementmicroservice.mappers.implementation.AdminMapper;
import com.aseds.usermanagementmicroservice.mappers.implementation.EmployeeMapper;
import com.aseds.usermanagementmicroservice.mappers.implementation.ProjectManagerMapper;
import com.aseds.usermanagementmicroservice.mappers.implementation.UserMapperDTO;
import com.aseds.usermanagementmicroservice.model.AbstractUser;
import com.aseds.usermanagementmicroservice.model.dto.RegisterRequest;
import com.aseds.usermanagementmicroservice.model.dto.UserDTO;
import com.aseds.usermanagementmicroservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final ProjectManagerMapper projectManagerMapper;
    private final AdminMapper adminMapper;
    private final UserMapperDTO userMapperDTO;
    private final EmployeeMapper employeeMapper;

    @Autowired
    public AuthService(UserRepository userRepository, ProjectManagerMapper projectManagerMapper, AdminMapper adminMapper, UserMapperDTO userMapperDTO, EmployeeMapper employeeMapper) {
        this.userRepository = userRepository;
        this.projectManagerMapper = projectManagerMapper;
        this.adminMapper = adminMapper;
        this.userMapperDTO = userMapperDTO;
        this.employeeMapper = employeeMapper;
    }

    public boolean isUserExists(String identifier) {
        if (isNotValidIdentifier(identifier)) {
            return false;
        }
        return userRepository.findUserByEmail(identifier).isPresent() ;
    }

    private boolean isNotValidIdentifier(String identifier) {
        return identifier == null || identifier.isBlank();
    }

    public UserDTO registerUser(RegisterRequest registerRequest) {
        validateUserRegistration(registerRequest);

        Roles role = determineUserRole(registerRequest.getRole());
        AbstractUser user = createUser(registerRequest, role);
        AbstractUser savedUser = userRepository.save(user);

        return userMapperDTO.mapTo(savedUser);
    }

    private void validateUserRegistration(RegisterRequest registerRequest) {
        if (registerRequest.getEmail() != null && isUserExists(registerRequest.getEmail())) {
            throw new IllegalArgumentException("User with this email already exists");
        }

    }

    private Roles determineUserRole(Roles requestedRole) {
        return switch (requestedRole) {
            case ADMIN -> Roles.ADMIN;
            case PROJECT_MANAGER -> Roles.PROJECT_MANAGER;
            case EMPLOYEE -> Roles.EMPLOYEE;
            default -> throw new IllegalArgumentException("Invalid role");
        };
    }

    private AbstractUser createUser(RegisterRequest registerRequest, Roles role) {
        AbstractUser user = switch (role) {
            case ADMIN -> adminMapper.mapFrom(registerRequest);
            case PROJECT_MANAGER -> projectManagerMapper.mapFrom(registerRequest);
            default -> employeeMapper.mapFrom(registerRequest);
        };

        user.setCreatedAt(new Date());
        user.setRole(role);

        return user;
    }



    public AbstractUser getUserByIdentifier(String identifier) {
        if (isNotValidIdentifier(identifier)) {
            throw new IllegalArgumentException("Invalid identifier: cannot be null or blank");
        }

        return findUserByEmailOrPhone(identifier);
    }

    private AbstractUser findUserByEmailOrPhone(String identifier) {
        return userRepository.findUserByEmail(identifier)

                        .orElseThrow(() -> new IllegalArgumentException("User not found with identifier: " + identifier));
    }



}
