package com.aseds.userauthmicroservice.services;

import com.aseds.userauthmicroservice.api.UserManagementClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {
    private final UserManagementClient userManagementClient;

    @Autowired
    public UserDetailService(UserManagementClient userManagementClient) {
        this.userManagementClient = userManagementClient;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userManagementClient.getUserByIdentifier(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with identifier: " + username));
    }

}