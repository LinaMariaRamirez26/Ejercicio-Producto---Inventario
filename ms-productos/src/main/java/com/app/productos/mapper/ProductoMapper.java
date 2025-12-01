package com.app.productos.mapper;

import com.app.productos.domain.Producto;
import com.app.productos.dto.ProductoRequest;
import com.app.productos.dto.ProductoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    Producto toEntity(ProductoRequest request);

    ProductoResponse toResponse(Producto product);

    void updateEntityFromRequest(ProductoRequest request, @MappingTarget Producto product);
}

