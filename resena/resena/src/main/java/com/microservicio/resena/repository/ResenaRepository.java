package com.microservicio.resena.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservicio.resena.model.Resena;

public interface ResenaRepository extends JpaRepository<Resena, Long> {
    List<Resena> findByProductoId(Long productoId);
    List<Resena> findByUsuarioId(Long usuarioId);

}
