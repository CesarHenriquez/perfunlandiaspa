package com.microservicio.registro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservicio.registro.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {
     Optional<Rol> findByNombre(String nombre);

}
