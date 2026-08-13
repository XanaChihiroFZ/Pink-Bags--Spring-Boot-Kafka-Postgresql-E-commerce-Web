package com.ecommerce.pinkbags.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.pinkbags.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByIdIn(List<Long> ids);         // For trending products
    List<Product> findByDiscountGreaterThan(Integer discount);  // For on-sale products

     @Query(value = "SELECT * FROM products ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<Product> findRandomProducts(@Param("limit") int limit);

    public List<Product> findTop4ByDiscountGreaterThan(double d);
}