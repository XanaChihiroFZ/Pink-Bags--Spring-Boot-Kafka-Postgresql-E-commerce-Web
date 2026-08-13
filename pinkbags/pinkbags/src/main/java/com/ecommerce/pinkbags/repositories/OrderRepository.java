package com.ecommerce.pinkbags.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.pinkbags.entities.Customer;
import com.ecommerce.pinkbags.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(Customer customer);
    List<Order> findByComplete(boolean complete);

    public Optional<Order> findByCustomerAndCompleteFalse(Customer customer);

    
    Optional<Order> findTopByCustomerAndCompleteTrueOrderByIdDesc(Customer customer);

    public boolean existsByCustomerAndCompleteFalse(Customer customer);
}