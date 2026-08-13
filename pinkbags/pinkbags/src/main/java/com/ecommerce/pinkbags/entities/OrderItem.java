package com.ecommerce.pinkbags.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private int quantity = 0;

    private LocalDateTime dateAdded = LocalDateTime.now();

    

    // Constructors
    public OrderItem() {}

    public OrderItem(Product product, Order order, int quantity) {
        this.product = product;
        this.order = order;
        this.quantity = quantity;
    }

    public OrderItem(Order order, Product product, int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public LocalDateTime getDateAdded() { return dateAdded; }
    public void setDateAdded(LocalDateTime dateAdded) { this.dateAdded = dateAdded; }

    // Derived property
    public double getTotal() {
        return product != null ? product.getPrice().doubleValue() * quantity : 0.0;
    }

    @Override
    public String toString() {
        return quantity + " x " + (product != null ? product.getName() : "Unknown Product");
    }

    public double setTotalPrice() {
        return product != null ? product.getPrice().doubleValue() * quantity : 0.0;
    }
    // Derived property
    public BigDecimal getTotalPrice() {
    if (product != null) {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
    return BigDecimal.ZERO;
}

}
