package com.app.productos.service;

import com.app.productos.domain.Producto;
import com.app.productos.dto.ProductoRequest;
import com.app.productos.dto.ProductoResponse;
import com.app.productos.mapper.ProductoMapper;
import com.app.productos.repository.ProductoRepository;
import com.app.productos.utilities.Constantes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository repository;

    @Mock
    private ProductoMapper mapper;

    @InjectMocks
    private ProductoService service;

    private Producto producto;
    private ProductoRequest request;
    private ProductoResponse response;

    @BeforeEach
    void setup() {
        producto = new Producto();
        producto.setId(Constantes.PRUEBA_ID_PRODUCTO);
        producto.setNombre("Pan");
        producto.setPrecio(300.0);
    }

    @Test
    @DisplayName("Crear Producto")
    void crearProducto() {
        when(mapper.toEntity(request)).thenReturn(producto);
        when(mapper.toResponse(producto)).thenReturn(response);

        ProductoResponse result = service.crearproducto(request);

        assertEquals(response, result);
        verify(repository).save(producto);
    }

    @Test
    @DisplayName("Obtener producto por su ID")
    void obtenerPorId() {
        when(repository.findById(Constantes.PRUEBA_ID_PRODUCTO)).thenReturn(Optional.of(producto));
        when(mapper.toResponse(producto)).thenReturn(response);

        ProductoResponse result = service.getById(Constantes.PRUEBA_ID_PRODUCTO);

        assertEquals(response, result);
        verify(repository).findById(Constantes.PRUEBA_ID_PRODUCTO);
    }

    @Test
    @DisplayName("Listar productos")
    void listarProductos() {
        when(repository.findAll()).thenReturn(List.of(producto));
        when(mapper.toResponse(producto)).thenReturn(response);

        List<ProductoResponse> result = service.listarProductos();

        assertEquals(1, result.size());
        assertEquals(response, result.get(0));
    }


    @Test
    @DisplayName("Eliminar productos")
    void eliminarProducto() {
        when(repository.existsById(Constantes.PRUEBA_ID_PRODUCTO)).thenReturn(true);
        service.eliminarProducto(Constantes.PRUEBA_ID_PRODUCTO);
        verify(repository).deleteById(Constantes.PRUEBA_ID_PRODUCTO);
    }
}
