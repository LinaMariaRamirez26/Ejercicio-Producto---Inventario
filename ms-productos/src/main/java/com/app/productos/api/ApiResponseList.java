package com.app.productos.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ApiResponseList<T> {
    private List<ApiResource<T>> data;
}

