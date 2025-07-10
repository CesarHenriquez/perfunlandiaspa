package com.microservicio.venta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.microservicio.venta.model.DetalleVenta;
import com.microservicio.venta.repository.DetalleVentaRepository;

@Service
public class DetalleVentaService {
    private final DetalleVentaRepository detalleVentaRepository;

    public DetalleVentaService(DetalleVentaRepository detalleVentaRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
    }

    public List<DetalleVenta> listarDetalles() {
        return detalleVentaRepository.findAll();
    }

    public DetalleVenta obtenerPorId(Long id) {
        return detalleVentaRepository.findById(id).orElse(null);
    }

}
