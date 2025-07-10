package com.microservicio.direccion.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.microservicio.direccion.model.Comuna;
import com.microservicio.direccion.repository.ComunaRepository;

@Service
public class ComunaService {
    private final ComunaRepository comunaRepository;

    public ComunaService(ComunaRepository comunaRepository) {
        this.comunaRepository = comunaRepository;
    }

    public List<Comuna> listar() {
        return comunaRepository.findAll();
    }

    public List<Comuna> listarPorRegion(Long regionId) {
        return comunaRepository.findByRegionId(regionId);
    }

    public Comuna guardar(Comuna comuna) {
        return comunaRepository.save(comuna);
    }

}
