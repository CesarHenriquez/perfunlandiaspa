package com.microservicio.privilegio.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.microservicio.privilegio.model.Privilegio;

public interface PrivilegioRepository extends JpaRepository<Privilegio, Long> {
   

}
