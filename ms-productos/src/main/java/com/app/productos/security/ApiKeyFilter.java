package com.app.productos.security;

import com.app.productos.utilities.Constantes;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Filtro de seguridad encargado de validar la API Key enviada en cada solicitud.
 */
@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${security.api-key}")
    private String configuredApiKey;

    private static final String HEADER_NAME = Constantes.LLAVE_API;

    /**
     * Determina si el filtro debe ejecutarse o no.
     * @param request
     * @return
     * @throws ServletException
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return "OPTIONS".equalsIgnoreCase(request.getMethod())
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/actuator");
    }

    /**
     * Lógica principal del filtro.
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String apiKey = request.getHeader(HEADER_NAME);
        if (apiKey == null || !apiKey.equals(configuredApiKey)) {
            response.setHeader("Access-Control-Allow-Origin", Constantes.RUTA_PROYECTO);
            response.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "X-API-Key,Content-Type,Accept");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            String body = "{\"errors\":[{\"status\":\"401\",\"title\":\"Unauthorized\",\"detail\":\"Missing or invalid API key\"}]}";
            response.getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));
            return;
        }
        filterChain.doFilter(request, response);
    }
}

