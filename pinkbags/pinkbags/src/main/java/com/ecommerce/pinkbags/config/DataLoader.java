package com.ecommerce.pinkbags.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ecommerce.pinkbags.entities.Customer;
import com.ecommerce.pinkbags.entities.User;
import com.ecommerce.pinkbags.repositories.CustomerRepository;
import com.ecommerce.pinkbags.repositories.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create a default admin user if none exists
        if (userRepository.findByUsername("admin@pinkbags.com").isEmpty()) {
            User user = new User();
            user.setUsername("admin@pinkbags.com");
            user.setEmail("admin@pinkbags.com");
            user.setPassword(passwordEncoder.encode("admin123"));
            user.setRole("ADMIN");
            userRepository.save(user);

            Customer customer = new Customer();
            customer.setUser(user);
            customer.setEmail("admin@pinkbags.com");
            customerRepository.save(customer);
            
            System.out.println("Default admin user created: admin@pinkbags.com / admin123");
        }
    }
}