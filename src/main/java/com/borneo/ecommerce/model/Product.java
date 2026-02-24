package com.borneo.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private int price;
    private int stock;


    private String imagePath;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")   
    @JsonBackReference
    private Category category;

    private String barcodeNumber; //   new column

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("product")   
    private Set<Wishlist> wishlists = new HashSet<>();
}