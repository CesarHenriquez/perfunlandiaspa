package com.microservicio.direccion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservicio.direccion.model.Comuna;

public interface ComunaRepository extends JpaRepository<Comuna, Long> {
     List<Comuna> findByRegionId(Long regionId);

}
