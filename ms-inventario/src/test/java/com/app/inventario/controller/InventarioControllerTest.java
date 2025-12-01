package com.app.inventario.controller;

import com.app.inventario.dto.*;
import com.app.inventario.service.InventarioService;
import com.app.inventario.utilities.Constantes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventarioControllerTest {

    @Mock
    private InventarioService inventarioService;

    @InjectMocks
    private InventarioController controller;

    private CreateInventarioRequest requestCompra;
    private InventarioAttributes inventarioAtributos;

    @BeforeEach
    void setup() {

        requestCompra = new CreateInventarioRequest();
        requestCompra.setProductId(Constantes.PRUEBA_ID_PRODUCTO);
        requestCompra.setCantidad(Constantes.CANTIDAD_COMPRA);

        inventarioAtributos = new InventarioAttributes();
        inventarioAtributos.setProductId(Constantes.PRUEBA_ID_PRODUCTO);
        inventarioAtributos.setCantidad(2);
    }

    @Test
    @DisplayName("Consultar stock retorna respuesta JSON API correctamente")
    void obtenerStock() {

        when(inventarioService.obtenerStockProducto(Constantes.PRUEBA_ID_PRODUCTO)).thenReturn(10);

        ResponseEntity<ApiResponse<StockResponse>> response =
                controller.obtenerStock(Constantes.PRUEBA_ID_PRODUCTO);

        ApiResponse<StockResponse> body = response.getBody();
        assertNotNull(body);

        ApiResource<StockResponse> resource = body.getData();
        assertEquals("1", resource.getId());
        assertEquals("inventario", resource.getType());

        StockResponse stockAttr = resource.getAttributes();
        assertEquals(Constantes.PRUEBA_ID_PRODUCTO, stockAttr.getProductId());
        assertEquals(10, stockAttr.getStock());

        verify(inventarioService).obtenerStockProducto(Constantes.PRUEBA_ID_PRODUCTO);
    }

    @Test
    @DisplayName("Compra del producto correctamente")
    void comprarProducto() {

        when(inventarioService.procesarCompra(requestCompra))
                .thenReturn(inventarioAtributos);

        ResponseEntity<ApiResponse<InventarioAttributes>> response =
                controller.compraProducto(requestCompra);

        ApiResponse<InventarioAttributes> body = response.getBody();
        assertNotNull(body);

        ApiResource<InventarioAttributes> resource = body.getData();

        assertNull(resource.getId());
        assertEquals("inventario", resource.getType());

        InventarioAttributes attr = resource.getAttributes();
        assertEquals(Constantes.PRUEBA_ID_PRODUCTO, attr.getProductId());
        assertEquals(2, attr.getCantidad());

        verify(inventarioService).procesarCompra(requestCompra);
    }

    @Test
    @DisplayName("Compra del producto fallida")
    void comprarProductoFallida() {

        when(inventarioService.procesarCompra(requestCompra))
                .thenThrow(new RuntimeException(Constantes.STOCK_INSUFICIENTE));

        assertThrows(
                RuntimeException.class,
                () -> controller.compraProducto(requestCompra)
        );

        verify(inventarioService).procesarCompra(requestCompra);
    }

}
