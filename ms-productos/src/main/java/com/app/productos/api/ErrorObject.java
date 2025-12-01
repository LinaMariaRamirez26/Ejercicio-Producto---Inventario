package com.app.productos.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ErrorObject {
    public String status;
    public String title;
    public String detail;
}

