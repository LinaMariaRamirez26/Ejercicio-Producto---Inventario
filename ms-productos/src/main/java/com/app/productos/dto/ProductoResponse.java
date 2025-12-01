package com.app.productos.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductoResponse {
    private Long id;
    private String nombre;
    private Double precio;
    private Integer stock;
}
