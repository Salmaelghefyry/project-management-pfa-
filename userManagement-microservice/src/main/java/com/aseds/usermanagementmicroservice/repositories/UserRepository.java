package com.aseds.usermanagementmicroservice.repositories;

import com.aseds.usermanagementmicroservice.model.AbstractUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AbstractUser, Long>, PagingAndSortingRepository<AbstractUser ,Long> {
    Optional<AbstractUser> findUserByEmail(String email);
}
