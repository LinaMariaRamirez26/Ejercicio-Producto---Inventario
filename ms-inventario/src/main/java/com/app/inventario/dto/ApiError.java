package com.app.inventario.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private String status;
    private String title;
    private String detail;
}

