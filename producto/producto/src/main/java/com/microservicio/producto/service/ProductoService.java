package com.microservicio.producto.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.microservicio.producto.model.Categoria;
import com.microservicio.producto.model.Producto;
import com.microservicio.producto.repository.CategoriaRepository;
import com.microservicio.producto.repository.ProductoRepository;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public Producto guardarProducto(Producto producto) {
       
        Long categoriaId = producto.getCategoria().getId();
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + categoriaId));

        producto.setCategoria(categoria);
        return productoRepository.save(producto);
    }

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }

}
