package com.app.productos.controller;

import com.app.productos.api.*;
import com.app.productos.service.ProductoService;
import com.app.productos.dto.ProductoRequest;
import com.app.productos.dto.ProductoResponse;
import com.app.productos.utilities.Constantes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST responsable de gestionar los productos del sistema.
 *
 * Este controlador expone endpoints CRUD siguiendo el estándar de respuesta JSON API
 * mediante el uso de las clases {@link ApiResponse}, {@link ApiResponseList} y {@link ApiResource}.
 *
 */
@RestController
@RequestMapping("/productos")
@Tag(name = "product-controller")
@SecurityRequirement(name = Constantes.METODO_AUTENTICACION)
public class ProductoController {

    private final ProductoService productService;
    private static final String TYPE = "productos";

    public ProductoController(ProductoService productService) {
        this.productService = productService;
    }

    /**
     * Lista de los productos que se encuentran registrados
     * @return
     */
    @Operation(summary = "Listar productos")
    @GetMapping(produces = Constantes.TIPO_CONTENIDO)
    public ResponseEntity<ApiResponseList<ProductoResponse>> list() {

        var resources = productService.listarProductos().stream()
                .map(prod -> toResource(prod.getId(), prod))
                .toList();

        return ResponseEntity.ok(new ApiResponseList<>(resources));
    }


    /**
     * Metodo para obtener un producto por su ID
     * @param id
     * @return
     */
    @Operation(summary = "Obtener producto por ID")
    @GetMapping(value = "/{id}", produces = Constantes.TIPO_CONTENIDO)
    public ResponseEntity<ApiResponse<ProductoResponse>> obtenerId(@PathVariable Long id) {

        var product = productService.getById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(toResource(id, product))
        );
    }

    /**
     * Metodo para crear un producto
     * @param req
     * @return
     */
    @Operation(summary = "Crear producto")
    @PostMapping(consumes = Constantes.TIPO_CONTENIDO, produces = Constantes.TIPO_CONTENIDO)
    public ResponseEntity<ApiResponse<ProductoResponse>> crearProducto(@RequestBody ProductoRequest req) {

        var created = productService.crearproducto(req);

        return ResponseEntity.ok(
                new ApiResponse<>(toResource(created.getId(), created))
        );
    }

    /**
     * Metodo para eliminar un producto
     * @param id
     * @return
     */
    @Operation(summary = "Eliminar producto")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Metodo para actualizar un producto
     * @param id
     * @param req
     * @return
     */
    @Operation(summary = "Actualizar producto")
    @PutMapping(value = "/{id}", consumes = Constantes.TIPO_CONTENIDO, produces = Constantes.TIPO_CONTENIDO)
    public ResponseEntity<ApiResponse<ProductoResponse>> actualizarProducto(
            @PathVariable Long id,
            @RequestBody ProductoRequest req
    ) {

        var updated = productService.actualizarProducto(id, req);

        return ResponseEntity.ok(
                new ApiResponse<>(toResource(updated.getId(), updated))
        );
    }

    /**
     * Convierte un {@link ProductoResponse} en un recurso compatible con JSON API.
     * @param id
     * @param data
     * @return
     */
    private ApiResource<ProductoResponse> toResource(Long id, ProductoResponse data) {
        return new ApiResource<>(
                id.toString(),
                TYPE,
                data
        );
    }
}

