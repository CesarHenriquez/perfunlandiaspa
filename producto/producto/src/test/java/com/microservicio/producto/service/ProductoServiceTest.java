package com.microservicio.producto.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.microservicio.producto.model.Categoria;
import com.microservicio.producto.model.Producto;
import com.microservicio.producto.repository.CategoriaRepository;
import com.microservicio.producto.repository.ProductoRepository;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {
      @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void guardarProducto_deberiaGuardarProductoConCategoriaCargada() {
        Categoria categoria = new Categoria(1L, "Perfumes");
        Producto producto = new Producto(null, "Aqua", "Fresco", 25000.0, categoria);

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(productoRepository.save(producto)).thenReturn(producto);

        Producto resultado = productoService.guardarProducto(producto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNombre()).isEqualTo("Aqua");
        assertThat(resultado.getCategoria().getNombre()).isEqualTo("Perfumes");
    }

    @Test
    void listarProductos_deberiaRetornarListaDesdeRepositorio() {
        Categoria cat = new Categoria(1L, "Fragancia");
        List<Producto> lista = Arrays.asList(
                new Producto(1L, "A", "desc", 1000.0, cat),
                new Producto(2L, "B", "desc", 2000.0, cat)
        );

        when(productoRepository.findAll()).thenReturn(lista);

        List<Producto> resultado = productoService.listarProductos();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNombre()).isEqualTo("A");
    }

    @Test
    void obtenerPorId_deberiaRetornarProductoSiExiste() {
        Categoria cat = new Categoria(1L, "Perfume");
        Producto producto = new Producto(1L, "Essence", "Delicado", 18000.0, cat);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Optional<Producto> resultado = productoService.obtenerPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("Essence");
    }

    @Test
    void eliminarProducto_deberiaLlamarADeleteById() {
        Long idProducto = 1L;

        productoService.eliminarProducto(idProducto);

        verify(productoRepository).deleteById(idProducto);
    }
   
    

}
