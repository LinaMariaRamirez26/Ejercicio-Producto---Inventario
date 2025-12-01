package com.app.productos.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
public class ResourceObject {
    public String type;
    public String id;
    public Map<String, Object> attributes;
}

