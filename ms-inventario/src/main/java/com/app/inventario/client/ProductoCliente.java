package com.app.inventario.client;

import com.app.inventario.dto.ApiResponse;
import com.app.inventario.dto.ProductoResponse;
import com.app.inventario.dto.ProductoPutRequest;
import com.app.inventario.utilities.Constantes;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;

/**
 * Cliente REST encargado de comunicarse con el microservicio productos-service.
 *
 * Realiza solicitudes HTTP para obtener y actualizar productos,aplicando reintentos automáticos
 *
 */
@Component
@RequiredArgsConstructor
public class ProductoCliente {

    private final org.springframework.web.client.RestTemplate restTemplate;

    @Value("${productos.service.url}")
    private String productsUrl;

    @Value("${productos.service.api.key}")
    private String apiKey;

    @Retryable(
            value = { ResourceAccessException.class, HttpServerErrorException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000)
    )
    public ProductoResponse getProduct(Long productId) {

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constantes.LLAVE_API, apiKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<ApiResponse<ProductoResponse>> response = restTemplate.exchange(
                    productsUrl + "/productos/" + productId,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<ApiResponse<ProductoResponse>>() {}
            );

            ApiResponse<ProductoResponse> body = response.getBody();
            if (body == null || body.getData() == null) {
                throw new RuntimeException(Constantes.RESPUESTA_INVALIDA_SERVIDOR);
            }
            return body.getData().getAttributes();

        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error desde productos-service: " + e.getStatusCode());
        } catch (ResourceAccessException e) {
            throw new RuntimeException(Constantes.TIMEOUT);
        }
    }

    @Retryable(
            value = { ResourceAccessException.class, HttpServerErrorException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000)
    )
    public ProductoResponse updateProductStockPut(Long productId, Integer newStock) {

        ProductoResponse current = getProduct(productId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(Constantes.LLAVE_API, apiKey);

        ProductoPutRequest body = new ProductoPutRequest();
        body.setNombre(current.getNombre());
        body.setPrecio(current.getPrecio());
        body.setStock(newStock);

        HttpEntity<ProductoPutRequest> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<ApiResponse<ProductoResponse>> response = restTemplate.exchange(
                    productsUrl + "/productos/" + productId,
                    HttpMethod.PUT,
                    entity,
                    new ParameterizedTypeReference<ApiResponse<ProductoResponse>>() {}
            );

            ApiResponse<ProductoResponse> resp = response.getBody();
            if (resp == null || resp.getData() == null) {
                throw new RuntimeException(Constantes.RESPUESTA_INVALIDA_SERVIDOR);
            }
            return resp.getData().getAttributes();

        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error desde productos-service: " + e.getStatusCode());
        } catch (ResourceAccessException e) {
            throw new RuntimeException(Constantes.TIMEOUT);
        }
    }
}
