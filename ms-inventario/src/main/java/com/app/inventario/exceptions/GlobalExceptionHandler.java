package com.app.inventario.exceptions;

import com.app.inventario.dto.ApiError;
import com.app.inventario.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleRuntime(RuntimeException ex) {
        ApiError error = new ApiError(
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                "Bad Request",
                ex.getMessage()
        );
        return ResponseEntity.badRequest().body(new ApiErrorResponse(List.of(error)));
    }
}

