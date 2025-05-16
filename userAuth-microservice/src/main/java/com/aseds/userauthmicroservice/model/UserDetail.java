package com.aseds.userauthmicroservice.model;

import com.aseds.userauthmicroservice.enums.Roles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public record UserDetail(AbstractUser user) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> user.getRole().name());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getPrenom() {
        return user.getPrenom();
    }
    public String getNom() {
        return user.getNom();
    }

    public Long getId() {
        return user.getId();
    }


    @Override
    public AbstractUser user() {
        return this.user;
    }
    public Roles getRole() {
        return user.getRole();
    }
}
