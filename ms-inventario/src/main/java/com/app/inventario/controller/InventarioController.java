package com.app.inventario.controller;

import com.app.inventario.dto.*;
import com.app.inventario.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador encargado de exponer los endpoints relacionados con el
 * inventario de productos. Gestiona consultas y actualizaciones de stock,
 * retornando las respuestas bajo el estándar JSON:API.
 */
@RestController
@RequestMapping("/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventoryService;

    /**
     * Consulta la cantidad disponible (stock) de un producto específico.
     * @param productId
     * @return
     */
    @Operation(summary = "Consultar la cantidad de un producto")
    @GetMapping("/{productId}/stock")
    public ResponseEntity<ApiResponse<StockResponse>> obtenerStock(@PathVariable Long productId) {
        Integer stock = inventoryService.obtenerStockProducto(productId);
        StockResponse attr = new StockResponse();
        attr.setProductId(productId);
        attr.setStock(stock);
        ApiResponse<StockResponse> response = buildResponse(
                productId.toString(),
                "inventario",
                attr
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Procesa una compra que disminuye el stock disponible de un producto.
     * Esta operación requiere una API Key configurada en el cliente que consume el servicio.
     * @param req
     * @return
     */
    @Operation(summary = "Actualizar cantidad disponible tras una compra")
    @PostMapping("/compra")
    @SecurityRequirement(name = "apiKey")
    public ResponseEntity<ApiResponse<InventarioAttributes>> compraProducto(
            @RequestBody CreateInventarioRequest req) {
        InventarioAttributes result = inventoryService.procesarCompra(req);
        ApiResponse<InventarioAttributes> response = buildResponse(
                null,
                "inventario",
                result
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Construye una respuesta estándar bajo el formato JSON:API.
     * @param id
     * @param type
     * @param attributes
     * @return
     * @param <T>
     */
    private <T> ApiResponse<T> buildResponse(String id, String type, T attributes) {
        ApiResource<T> resource = new ApiResource<>(id, type, attributes);
        return new ApiResponse<>(resource);
    }
}

