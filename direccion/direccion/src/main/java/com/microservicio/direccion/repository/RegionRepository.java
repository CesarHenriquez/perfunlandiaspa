package com.microservicio.direccion.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservicio.direccion.model.Region;

public interface RegionRepository extends JpaRepository<Region, Long> {

}
