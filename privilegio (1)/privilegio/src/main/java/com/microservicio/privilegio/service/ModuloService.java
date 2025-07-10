package com.microservicio.privilegio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.microservicio.privilegio.model.Modulo;
import com.microservicio.privilegio.repository.ModuloRepository;

@Service
public class ModuloService {
    private final ModuloRepository moduloRepository;

    public ModuloService(ModuloRepository moduloRepository) {
        this.moduloRepository = moduloRepository;
    }

    public List<Modulo> listarModulos() {
        return moduloRepository.findAll();
    }

    public Optional<Modulo> obtenerPorId(Long id) {
        return moduloRepository.findById(id);
    }

    public Modulo guardarModulo(Modulo modulo) {
        return moduloRepository.save(modulo);
    }

    public Modulo actualizarModulo(Long id, Modulo moduloActualizado) {
        Modulo existente = moduloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado con ID: " + id));
        existente.setNombre(moduloActualizado.getNombre());
        return moduloRepository.save(existente);
    }

    public void eliminarModulo(Long id) {
        moduloRepository.deleteById(id);
    }

}
