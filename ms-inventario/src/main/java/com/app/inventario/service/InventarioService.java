package com.app.inventario.service;

import com.app.inventario.client.ProductoCliente;
import com.app.inventario.domain.Inventario;
// removed wrong import
import com.app.inventario.dto.CreateInventarioRequest;
import com.app.inventario.dto.InventarioAttributes;
import com.app.inventario.dto.ProductoResponse;
import com.app.inventario.repository.InventarioRepository;
import com.app.inventario.utilities.Constantes;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.bcel.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado de gestionar la lógica del inventario local y su sincronización
 * con el microservicio de productos. Permite consultar stock, validar disponibilidad y procesar compras.
 */
@Service
@RequiredArgsConstructor
public class InventarioService {

    private static final Logger log = LoggerFactory.getLogger(InventarioService.class);
    private final InventarioRepository repository;
    private final ProductoCliente productClient;

    /**
     * Obtiene el stock actual de un producto consultando el microservicio de productos.
     * @param productId
     * @return
     */
    public Integer obtenerStockProducto(Long productId) {
        ProductoResponse product = obtenerProductoExterno(productId);
        return product.getStock();
    }

    /**
     *  Procesa la compra de un producto
     * @param req
     * @return
     */
    public InventarioAttributes procesarCompra(CreateInventarioRequest req) {
        ProductoResponse product = obtenerProductoExterno(req.getProductId());
        Inventario inv = obtenerInventarioSincronizado(req.getProductId(), product.getStock());
        validarStockSuficiente(inv.getCantidad(), req.getCantidad());
        int nuevoStock = calcularNuevoStock(product.getStock(), req.getCantidad());
        actualizarStockProductoRemoto(req.getProductId(), nuevoStock);
        actualizarInventarioLocal(inv, nuevoStock);
        logCambioInventario(req.getProductId(), product.getStock(), nuevoStock);
        return construirRespuesta(req.getProductId(), nuevoStock);
    }

    /**
     * Obtiene un producto desde el microservicio de productos o lanza excepción si no existe.
     * @param productId
     * @return
     */
    private ProductoResponse obtenerProductoExterno(Long productId) {
        ProductoResponse product = productClient.getProduct(productId);
        if (product == null) {
            throw new RuntimeException(Constantes.PRODUCTO_NO_ENCONTRADO);
        }
        return product;
    }

    /**
     * Sincroniza el inventario
     * @param productId
     * @param stockRemoto
     * @return
     */
    private Inventario obtenerInventarioSincronizado(Long productId, int stockRemoto) {
        return repository.findByProductId(productId)
                .orElseGet(() -> {
                    Inventario nuevo = new Inventario();
                    nuevo.setProductId(productId);
                    nuevo.setCantidad(stockRemoto);
                    return repository.save(nuevo);
                });
    }

    /**
     * Valida si existe stock suficiente para realizar la compra.
     * @param disponible
     * @param requerido
     */
    private void validarStockSuficiente(int disponible, int requerido) {
        if (disponible < requerido) {
            throw new RuntimeException(Constantes.STOCK_INSUFICIENTE);
        }
    }

    /**
     * Calcula el nuevo stock del producto y que no sea negativo.
     * @param stockActual
     * @param cantidad
     * @return
     */
    private int calcularNuevoStock(int stockActual, int cantidad) {
        return Math.max(0, stockActual - cantidad);
    }

    /**
     * Actualiza el stock en el microservicio de productos.
     * @param productId
     * @param nuevoStock
     */
    private void actualizarStockProductoRemoto(Long productId, int nuevoStock) {
        productClient.updateProductStockPut(productId, nuevoStock);
    }

    /**
     * Actualiza el registro del inventario con el nuevo stock.
     * @param inv
     * @param nuevoStock
     */
    private void actualizarInventarioLocal(Inventario inv, int nuevoStock) {
        inv.setCantidad(nuevoStock);
        repository.save(inv);
    }

    /**
     * Emitir un evento básico cuando cambia el inventario
     * @param productId
     * @param anterior
     * @param nuevo
     */
    private void logCambioInventario(Long productId, int anterior, int nuevo) {
        log.info("Inventario actualizado para producto {}: stock {} -> {}", productId, anterior, nuevo);
    }

    /**
     * Construye el objeto de respuesta para el controlador siguiendo el formato JSON:API.
     * @param productId
     * @param cantidad
     * @return
     */
    private InventarioAttributes construirRespuesta(Long productId, int cantidad) {
        InventarioAttributes attr = new InventarioAttributes();
        attr.setProductId(productId);
        attr.setCantidad(cantidad);
        return attr;
    }
}
