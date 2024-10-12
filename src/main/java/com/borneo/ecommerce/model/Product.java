package com.borneo.ecommerce.model;

import lombok.Data;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column(length =  1000)
    private BigDecimal price;

    @Column
    private Integer stock;

    @Column
    private String description;





}
