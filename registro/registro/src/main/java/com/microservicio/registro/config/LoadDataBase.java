package com.microservicio.registro.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.microservicio.registro.model.Rol;
import com.microservicio.registro.repository.RolRepository;

@Configuration
public class LoadDataBase {
    @Bean
     CommandLineRunner initDatabase(RolRepository rolRepository) {
        return args -> {
            if (rolRepository.count() == 0) {
                rolRepository.save(new Rol(null, "CLIENTE"));
                rolRepository.save(new Rol(null, "LOGISTICA"));
                rolRepository.save(new Rol(null, "GERENTE"));
                rolRepository.save(new Rol(null, "ADMIN"));
            }
        };
    }

}
