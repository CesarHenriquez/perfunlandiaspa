package com.microservicio.direccion.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.microservicio.direccion.model.Comuna;
import com.microservicio.direccion.model.Direccion;
import com.microservicio.direccion.repository.ComunaRepository;
import com.microservicio.direccion.repository.DireccionRepository;

@Service
public class DireccionService {
    private final DireccionRepository direccionRepository;
    private final ComunaRepository comunaRepository;

    public DireccionService(DireccionRepository direccionRepository, ComunaRepository comunaRepository) {
        this.direccionRepository = direccionRepository;
        this.comunaRepository = comunaRepository;
    }

    public Direccion guardar(Direccion direccion) {

        Long comunaId = direccion.getComuna().getId();
        Comuna comunaCompleta = comunaRepository.findById(comunaId).orElse(null);
        direccion.setComuna(comunaCompleta);

        return direccionRepository.save(direccion);
    }

    public List<Direccion> listar() {
        return direccionRepository.findAll();
    }

    public List<Direccion> buscarPorUsuario(Long usuarioId) {
        return direccionRepository.findByUsuarioId(usuarioId);
    }

}
