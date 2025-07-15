package com.diegonunez.Product_MS.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
     @Column(unique = true)
    private String name;
    private BigDecimal price;
}
