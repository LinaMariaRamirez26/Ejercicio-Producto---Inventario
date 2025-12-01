package com.app.productos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ProductoRequest {
    private String nombre;
    private Double precio;
    private Integer stock;
}

