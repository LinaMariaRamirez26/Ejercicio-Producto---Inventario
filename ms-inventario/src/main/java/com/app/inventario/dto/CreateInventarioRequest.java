package com.app.inventario.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateInventarioRequest {
    private Long productId;
    private Integer cantidad;
}

