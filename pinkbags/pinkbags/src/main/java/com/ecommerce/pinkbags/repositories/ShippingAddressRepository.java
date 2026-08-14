package com.ecommerce.pinkbags.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.pinkbags.entities.Customer;
import com.ecommerce.pinkbags.entities.Order;
import com.ecommerce.pinkbags.entities.ShippingAddress;

public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Long> {
    List<ShippingAddress> findByCustomer(Customer customer);
    Optional<ShippingAddress> findByOrderId(Long orderId);
    Optional<ShippingAddress> findByOrder(Order order);
}
