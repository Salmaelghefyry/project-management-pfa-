package com.aseds.userauthmicroservice.model;

import com.aseds.userauthmicroservice.enums.Roles;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    private String nom;
    private String username;
    private String password;
    private String email;
    private String prenom;
    private Roles role;
}