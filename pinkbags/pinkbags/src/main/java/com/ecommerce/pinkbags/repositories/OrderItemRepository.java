package com.ecommerce.pinkbags.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.pinkbags.entities.Customer;
import com.ecommerce.pinkbags.entities.Order;
import com.ecommerce.pinkbags.entities.OrderItem;
import com.ecommerce.pinkbags.entities.Product;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder(Order order);

    public Optional<OrderItem> findByIdAndOrderCustomerAndOrderCompleteFalse(Long itemId, Customer customer);

    Optional<OrderItem> findByOrderAndProduct(Order order, Product product);

}
