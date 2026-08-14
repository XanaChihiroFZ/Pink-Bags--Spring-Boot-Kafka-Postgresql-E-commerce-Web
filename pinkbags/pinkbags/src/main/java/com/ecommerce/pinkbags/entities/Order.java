package com.ecommerce.pinkbags.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(
    name = "orders",
    uniqueConstraints = {
        @jakarta.persistence.UniqueConstraint(
            name = "unique_incomplete_order_per_customer",
            columnNames = {"customer_id", "complete"}
        )
    }
)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status = "PENDING"; // Default

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private LocalDateTime dateOrdered = LocalDateTime.now();

    private boolean complete = false;

    @Column(length = 100)
    private String transactionId;

    // Relationships
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    // Constructors
    public Order() {}

    public Order(Customer customer) {
        this.customer = customer;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public LocalDateTime getDateOrdered() { return dateOrdered; }
    public void setDateOrdered(LocalDateTime dateOrdered) { this.dateOrdered = dateOrdered; }

    public boolean isComplete() { return complete; }
    public void setComplete(boolean complete) { this.complete = complete; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public List<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }

    // Derived properties
    public double getCartTotal() {
        return orderItems != null ? orderItems.stream()
                .mapToDouble(OrderItem::getTotal).sum() : 0.0;
    }

    public int getCartItems() {
        return orderItems != null ? orderItems.stream()
                .filter(item -> item.getProduct() != null)
                .mapToInt(OrderItem::getQuantity).sum() : 0;
    }

    public boolean isShipping() {
        return orderItems != null && !orderItems.isEmpty();
    }

    @Override
    public String toString() {
        return "Order " + id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUpdatedAt(LocalDateTime now) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
