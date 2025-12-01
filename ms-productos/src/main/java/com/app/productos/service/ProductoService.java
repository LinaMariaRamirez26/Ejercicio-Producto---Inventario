package com.app.productos.service;

import com.app.productos.domain.Producto;
import com.app.productos.mapper.ProductoMapper;
import com.app.productos.repository.ProductoRepository;
import com.app.productos.dto.ProductoRequest;
import com.app.productos.dto.ProductoResponse;
import com.app.productos.utilities.Constantes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Servicio encargado de manejar la lógica de negocio relacionada con los productos.
 *
 * Contiene métodos para crear, consultar, listar, actualizar y eliminar productos.
 */
@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productRepository;
    private final ProductoMapper mapper;

    /**
     * Crea un nuevo producto a partir de la información recibida.
     * @param req
     * @return
     */
    public ProductoResponse crearproducto(ProductoRequest req) {
        Producto p = mapper.toEntity(req);
        productRepository.save(p);
        return mapper.toResponse(p);
    }

    /**
     * Obtiene un producto por su ID.
     * @param id
     * @return
     */
    public ProductoResponse getById(Long id) {
        Producto p = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constantes.PRODUCTO_NO_ENCONTRADO));
        return mapper.toResponse(p);
    }


    /**
     * Lista todos los productos registrados en el sistema.
     * @return
     */
    public List<ProductoResponse> listarProductos() {
        return productRepository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    /**
     * Actualiza un producto existente.
     * @param id
     * @param req
     * @return
     */
    public ProductoResponse actualizarProducto(Long id, ProductoRequest req) {
        Producto p = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constantes.PRODUCTO_NO_ENCONTRADO));

        mapper.updateEntityFromRequest(req, p);
        productRepository.save(p);
        return mapper.toResponse(p);
    }

    /**
     * Elimina un producto por su ID.
     * @param id
     */
    public void eliminarProducto(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, Constantes.PRODUCTO_NO_ENCONTRADO);
        }
        productRepository.deleteById(id);
    }
}
