package com.app.inventario.security;

import com.app.inventario.utilities.Constantes;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${productos.service.api.key}")
    private String apiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String key = request.getHeader(Constantes.LLAVE_API);

        if ("POST".equalsIgnoreCase(request.getMethod())) {
            if (!apiKey.equals(key)) {
                response.setHeader("Access-Control-Allow-Origin", Constantes.RUTA_PROYECTO);
                response.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
                response.setHeader("Access-Control-Allow-Headers", "X-API-Key,Content-Type,Accept");
                response.setStatus(401);
                response.getWriter().write("{\"errors\":[{\"title\":\"Unauthorized\"}]} ");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}

