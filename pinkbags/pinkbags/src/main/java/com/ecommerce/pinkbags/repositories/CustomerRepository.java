package com.ecommerce.pinkbags.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.pinkbags.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);

    public Customer findByUserUsername(String username);

    public boolean existsByUserUsername(String email);

}
