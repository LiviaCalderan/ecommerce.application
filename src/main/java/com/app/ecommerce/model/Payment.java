package com.app.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


}