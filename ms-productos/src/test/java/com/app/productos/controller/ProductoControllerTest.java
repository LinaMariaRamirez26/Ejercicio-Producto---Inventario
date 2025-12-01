package com.app.productos.controller;

import com.app.productos.api.ApiResource;
import com.app.productos.api.ApiResponse;
import com.app.productos.api.ApiResponseList;
import com.app.productos.dto.ProductoRequest;
import com.app.productos.dto.ProductoResponse;
import com.app.productos.service.ProductoService;

import com.app.productos.utilities.Constantes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController controller;

    private ProductoResponse productoResponse;
    private ProductoRequest productoRequest;

    @BeforeEach
    void setUp() {
        productoResponse = new ProductoResponse(Constantes.PRUEBA_ID_PRODUCTO, Constantes.NOMBRE_PRODUCTO, Constantes.PRECIO_PRODUCTO, Constantes.STOCK);
        productoRequest = new ProductoRequest(Constantes.NOMBRE_PRODUCTO, Constantes.PRECIO_PRODUCTO, Constantes.STOCK);
    }

    @Test
    void listarProductos() {

        when(productoService.listarProductos()).thenReturn(List.of(productoResponse));

        ResponseEntity<ApiResponseList<ProductoResponse>> response = controller.list();

        assertEquals(1, response.getBody().getData().size());

        ApiResource<ProductoResponse> resource = response.getBody().getData().get(0);
        assertEquals("1", resource.getId());
        assertEquals("productos", resource.getType());
        assertEquals(Constantes.NOMBRE_PRODUCTO, resource.getAttributes().getNombre());

        verify(productoService).listarProductos();
    }

    @Test
    void obtenerIdProducto() {

        when(productoService.getById(Constantes.PRUEBA_ID_PRODUCTO)).thenReturn(productoResponse);

        ResponseEntity<ApiResponse<ProductoResponse>> response = controller.obtenerId(Constantes.PRUEBA_ID_PRODUCTO);

        assertNotNull(response.getBody());

        ApiResource<ProductoResponse> resource = response.getBody().getData();
        assertEquals("1", resource.getId());
        assertEquals("productos", resource.getType());

        verify(productoService).getById(Constantes.PRUEBA_ID_PRODUCTO);
    }

    @Test
    void crearProductok() {

        when(productoService.crearproducto(productoRequest)).thenReturn(productoResponse);

        ResponseEntity<ApiResponse<ProductoResponse>> response = controller.crearProducto(productoRequest);

        ApiResource<ProductoResponse> resource = response.getBody().getData();
        assertEquals("1", resource.getId());
        assertEquals(Constantes.NOMBRE_PRODUCTO, resource.getAttributes().getNombre());
        verify(productoService).crearproducto(productoRequest);
    }

    @Test
    void eliminarProducto() {

        doNothing().when(productoService).eliminarProducto(Constantes.PRUEBA_ID_PRODUCTO);
        ResponseEntity<Void> response = controller.eliminarProducto(Constantes.PRUEBA_ID_PRODUCTO);
        verify(productoService).eliminarProducto(Constantes.PRUEBA_ID_PRODUCTO);
    }

    @Test
    void actualizarProducto() {

        when(productoService.actualizarProducto(Constantes.PRUEBA_ID_PRODUCTO, productoRequest)).thenReturn(productoResponse);
        ResponseEntity<ApiResponse<ProductoResponse>> response =
                controller.actualizarProducto(Constantes.PRUEBA_ID_PRODUCTO, productoRequest);
        ApiResource<ProductoResponse> resource = response.getBody().getData();
        assertEquals("1", resource.getId());
        assertEquals(Constantes.NOMBRE_PRODUCTO, resource.getAttributes().getNombre());
        verify(productoService).actualizarProducto(Constantes.PRUEBA_ID_PRODUCTO, productoRequest);
    }
}
