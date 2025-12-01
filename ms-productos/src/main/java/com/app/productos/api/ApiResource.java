package com.app.productos.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResource<T> {
    private String id;
    private String type;
    private T attributes;
}

