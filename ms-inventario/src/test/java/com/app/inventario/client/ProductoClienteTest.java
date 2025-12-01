package com.app.inventario.client;

import com.app.inventario.dto.ApiResource;
import com.app.inventario.dto.ApiResponse;
import com.app.inventario.dto.ProductoResponse;
import com.app.inventario.utilities.Constantes;
import org.apache.tomcat.util.bcel.Const;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import java.nio.charset.StandardCharsets;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@ExtendWith(MockitoExtension.class)
class ProductoClienteTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ProductoCliente cliente;

    private ProductoResponse producto;
    private ApiResponse<ProductoResponse> apiResponse;
    private ApiResource<ProductoResponse> apiResource;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(cliente, "productsUrl", "http://localhost:8080");
        ReflectionTestUtils.setField(cliente, "apiKey", Constantes.EJEMPLO_AUTENTICACION);
    }

    @Test
    @DisplayName("Producto retorna información correctamente")
    void obtenerProducto() {
        ProductoResponse producto = new ProductoResponse();
        producto.setId(Constantes.PRUEBA_ID_PRODUCTO);
        producto.setNombre(Constantes.NOMBRE_PRODUCTO);
        producto.setPrecio(Constantes.PRECIO_PRODUCTO);
        producto.setStock(Constantes.STOCK);

        ApiResource<ProductoResponse> resource =
                new ApiResource<>("1", "producto", producto);
        ApiResponse<ProductoResponse> apiResponse =
                new ApiResponse<>(resource);
        ResponseEntity<ApiResponse<ProductoResponse>> response =
                new ResponseEntity<>(apiResponse, HttpStatus.OK);

        String url = "http://localhost:8080/productos/1";

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constantes.LLAVE_API, Constantes.EJEMPLO_AUTENTICACION);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        when(restTemplate.exchange(
                eq(url),
                eq(HttpMethod.GET),
                eq(entity),
                any(ParameterizedTypeReference.class)
        )).thenReturn(response);

        ProductoResponse out = cliente.getProduct(Constantes.PRUEBA_ID_PRODUCTO);

        assertEquals(Constantes.NOMBRE_PRODUCTO, out.getNombre());
        assertEquals(Constantes.STOCK, out.getStock());
        verify(restTemplate).exchange(
                eq(url),
                eq(HttpMethod.GET),
                eq(entity),
                any(ParameterizedTypeReference.class)
        );
    }
}
