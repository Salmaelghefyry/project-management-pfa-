package com.aseds.userauthmicroservice.model;

import lombok.*;
import com.aseds.userauthmicroservice.enums.Roles;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String nom;
    private String username;
    private String password;
    private String email;
    private String prenom;
    private Roles role;
}
