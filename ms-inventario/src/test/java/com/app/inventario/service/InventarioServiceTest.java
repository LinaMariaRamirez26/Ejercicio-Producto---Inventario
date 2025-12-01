package com.app.inventario.service;

import com.app.inventario.client.ProductoCliente;
import com.app.inventario.domain.Inventario;
import com.app.inventario.dto.CreateInventarioRequest;
import com.app.inventario.dto.InventarioAttributes;
import com.app.inventario.dto.ProductoResponse;
import com.app.inventario.repository.InventarioRepository;
import com.app.inventario.utilities.Constantes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    @Mock InventarioRepository repository;
    @Mock ProductoCliente productClient;
    @InjectMocks InventarioService service;

    private CreateInventarioRequest requestCompra;
    private ProductoResponse producto;
    private Inventario inventario;

    @BeforeEach
    void setup() {
        requestCompra = new CreateInventarioRequest();
        requestCompra.setProductId(Constantes.PRUEBA_ID_PRODUCTO);
        requestCompra.setCantidad(3);

        producto = new ProductoResponse();
        producto.setId(Constantes.PRUEBA_ID_PRODUCTO);
        producto.setNombre(Constantes.NOMBRE_PRODUCTO);
        producto.setPrecio(Constantes.PRECIO_PRODUCTO);
        producto.setStock(5);

        inventario = new Inventario();
        inventario.setId(10L);
        inventario.setProductId(Constantes.PRUEBA_ID_PRODUCTO);
        inventario.setCantidad(5);
    }

    @Test
    @DisplayName("Procesar compra actualizar el stock del inventario")
    void procesaCompraActualizarStock() {

        when(productClient.getProduct(Constantes.PRUEBA_ID_PRODUCTO)).thenReturn(producto);
        when(repository.findByProductId(Constantes.PRUEBA_ID_PRODUCTO)).thenReturn(Optional.of(inventario));

        Inventario inventarioActualizado = new Inventario();
        inventarioActualizado.setId(10L);
        inventarioActualizado.setProductId(Constantes.PRUEBA_ID_PRODUCTO);
        inventarioActualizado.setCantidad(Constantes.CANTIDAD_COMPRA);

        when(repository.save(any(Inventario.class))).thenReturn(inventarioActualizado);
        when(productClient.updateProductStockPut(Constantes.PRUEBA_ID_PRODUCTO, Constantes.CANTIDAD_COMPRA)).thenReturn(producto);

        InventarioAttributes result = service.procesarCompra(requestCompra);

        assertEquals(Constantes.PRUEBA_ID_PRODUCTO, result.getProductId());
        assertEquals(Constantes.CANTIDAD_COMPRA, result.getCantidad());
        verify(productClient).updateProductStockPut(Constantes.PRUEBA_ID_PRODUCTO, Constantes.CANTIDAD_COMPRA);
        verify(repository).save(any(Inventario.class));
    }

    @Test
    @DisplayName("Procesar compra cuando el stock es insuficiente")
    void stockInsuficiente() {
        producto.setStock(4);
        inventario.setCantidad(1);

        when(productClient.getProduct(Constantes.PRUEBA_ID_PRODUCTO)).thenReturn(producto);
        when(repository.findByProductId(Constantes.PRUEBA_ID_PRODUCTO)).thenReturn(Optional.of(inventario));

        assertThrows(RuntimeException.class, () -> service.procesarCompra(requestCompra));
    }
}
