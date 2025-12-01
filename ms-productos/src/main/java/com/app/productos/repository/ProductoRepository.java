package com.app.productos.repository;

import com.app.productos.domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio encargado de las operaciones de la aplicacion
 */
public interface ProductoRepository extends JpaRepository<Producto, Long> {
}

