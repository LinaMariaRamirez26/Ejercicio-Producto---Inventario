package com.app.inventario.config;

import com.app.inventario.utilities.Constantes;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI/Swagger para definir el esquema de seguridad utilizado por el microservicio.
 * Esta clase registra un esquema de autenticación basado en API Key que se enviará
 * en el encabezado de cada solicitud. La definición realizada aquí permite que Swagger UI
 * muestre automáticamente un campo donde el usuario puede ingresar el API Key antes de probar los endpoints.
 *
 */
@Configuration
@SecurityScheme(
        name = "apiKey",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = Constantes.LLAVE_API
)
public class OpenApiConfig {
}

