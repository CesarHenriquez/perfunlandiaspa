package com.microservicio.privilegio.config;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.microservicio.privilegio.model.Modulo;
import com.microservicio.privilegio.model.Privilegio;
import com.microservicio.privilegio.repository.ModuloRepository;
import com.microservicio.privilegio.repository.PrivilegioRepository;

@Configuration
class LoadDataBase {
    @Bean
    CommandLineRunner initData(ModuloRepository moduloRepository, PrivilegioRepository privilegioRepository) {
        return args -> {
            if (moduloRepository.count() == 0) {
                Modulo productos = new Modulo(null, "Productos", null);
                Modulo ventas = new Modulo(null, "Ventas", null);
                Modulo resenas = new Modulo(null, "Reseñas", null);
                Modulo direcciones = new Modulo(null, "Direcciones", null);

                productos = moduloRepository.save(productos);
                ventas = moduloRepository.save(ventas);
                resenas = moduloRepository.save(resenas);
                direcciones = moduloRepository.save(direcciones);

                privilegioRepository.saveAll(Arrays.asList(
                        new Privilegio(null, "ADMIN", "Puede registrar productos", productos),
                        new Privilegio(null, "GERENTE", "Puede ver todas las ventas", ventas),
                        new Privilegio(null, "CLIENTE", "Puede realizar reseñas", resenas),
                        new Privilegio(null, "ADMIN", "Puede gestionar direcciones", direcciones)));
            }
        };
    }

}
