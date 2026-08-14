package com.ecommerce.pinkbags.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.pinkbags.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
