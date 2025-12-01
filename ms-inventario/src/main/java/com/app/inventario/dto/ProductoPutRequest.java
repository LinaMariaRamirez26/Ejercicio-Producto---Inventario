package com.app.inventario.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoPutRequest {
    private String nombre;
    private Double precio;
    private Integer stock;
}

