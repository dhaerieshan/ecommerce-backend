package com.borneo.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Self-referencing many-to-one relationship for parent category
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    // Self-referencing one-to-many relationship for subcategories
    @OneToMany(mappedBy = "parent")
    private List<Category> subcategories;

    // Existing relationship with products (optional)
    @OneToMany(mappedBy = "category")
    private List<Product> products;

    // Getters and Setters
    // ...

    // Override toString(), equals(), and hashCode() if necessary
}