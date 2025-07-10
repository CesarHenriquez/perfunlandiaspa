package com.microservicio.direccion.config;

import org.springframework.stereotype.Component;

import com.microservicio.direccion.model.Comuna;
import com.microservicio.direccion.model.Region;
import com.microservicio.direccion.repository.ComunaRepository;
import com.microservicio.direccion.repository.RegionRepository;

import jakarta.annotation.PostConstruct;

@Component
public class LoadDataBase {
    private final RegionRepository regionRepository;
    private final ComunaRepository comunaRepository;

    public LoadDataBase(RegionRepository regionRepository, ComunaRepository comunaRepository) {
        this.regionRepository = regionRepository;
        this.comunaRepository = comunaRepository;
    }

    @PostConstruct
    public void init() {
        if (regionRepository.count() == 0) {
            Region region = new Region();
            region.setNombre("Región Metropolitana");
            region = regionRepository.save(region);

            Comuna comuna1 = new Comuna();
            comuna1.setNombre("Santiago");
            comuna1.setRegion(region);
            comunaRepository.save(comuna1);

            Comuna comuna2 = new Comuna();
            comuna2.setNombre("Providencia");
            comuna2.setRegion(region);
            comunaRepository.save(comuna2);
        }
    }

}
