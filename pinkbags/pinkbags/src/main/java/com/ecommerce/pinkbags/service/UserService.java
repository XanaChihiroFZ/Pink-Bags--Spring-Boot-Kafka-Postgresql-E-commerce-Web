package com.ecommerce.pinkbags.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ecommerce.pinkbags.entities.User;
import com.ecommerce.pinkbags.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Create or Update
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Read all
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Read one
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Find by username
    public Optional<User> getUserByUsername(String username) {
    return userRepository.findByUsername(username); // ✅ returns Optional<User>
}


    // Delete
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
