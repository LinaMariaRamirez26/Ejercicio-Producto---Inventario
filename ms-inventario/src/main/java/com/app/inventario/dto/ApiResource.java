package com.app.inventario.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResource<T> {
    private String id;
    private String type;
    private T attributes;
}

