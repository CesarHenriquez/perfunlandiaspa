package com.microservicio.direccion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservicio.direccion.model.Direccion;

public interface DireccionRepository extends JpaRepository<Direccion, Long> {
     List<Direccion> findByUsuarioId(Long usuarioId);

}
