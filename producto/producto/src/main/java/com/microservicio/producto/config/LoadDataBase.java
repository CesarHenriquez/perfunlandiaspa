package com.microservicio.producto.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.microservicio.producto.model.Categoria;
import com.microservicio.producto.repository.CategoriaRepository;

@Configuration
public class LoadDataBase {
    @Bean
    CommandLineRunner initDatabase(CategoriaRepository categoriaRepository) {
        return args -> {
            if (categoriaRepository.count() == 0) {
                categoriaRepository.save(new Categoria(null, "Perfumes"));
                categoriaRepository.save(new Categoria(null, "Colonias"));
                categoriaRepository.save(new Categoria(null, "Aromas Unisex"));
            }
        };
    }

}
