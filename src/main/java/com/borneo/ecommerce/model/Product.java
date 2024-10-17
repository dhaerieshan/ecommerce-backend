package com.borneo.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(length =  1000)
    private BigDecimal price;

    @Column
    private Integer stock;

    @Column
    private String description;

    @Column
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;





}
