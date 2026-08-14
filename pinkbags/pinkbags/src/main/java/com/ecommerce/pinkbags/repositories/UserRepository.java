package com.ecommerce.pinkbags.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.pinkbags.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); 
    Optional<User> findByEmail(String email);
}
