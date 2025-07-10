package com.microservicio.privilegio.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.microservicio.privilegio.model.Modulo;

public interface ModuloRepository extends JpaRepository<Modulo, Long>{
    

}
