package com.app.inventario.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long productId;

    @Column(nullable = false)
    private Integer cantidad;
}

