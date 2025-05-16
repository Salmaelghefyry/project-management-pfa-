package com.aseds.usermanagementmicroservice.model;

import com.aseds.usermanagementmicroservice.enums.Roles;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "users")
@DiscriminatorColumn(name = "user_type")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "user_type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ProjectManagerEntity.class, name = "project_manager_entity"),
        @JsonSubTypes.Type(value = AdminEntity.class, name = "admin_entity"),
        @JsonSubTypes.Type(value = EmployeeEntity.class, name = "employee_entity")
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class AbstractUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;


    protected String nom;
    protected String prenom;
    protected String username;
    protected String password;

    @Column(unique = true, nullable = false)
    protected String email;



    @Enumerated(EnumType.STRING)
    protected Roles role;

    protected Date createdAt;
}
