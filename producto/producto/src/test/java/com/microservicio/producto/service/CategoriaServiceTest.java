package com.microservicio.producto.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.microservicio.producto.model.Categoria;
import com.microservicio.producto.repository.CategoriaRepository;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {
    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void listarCategorias_deberiaRetornarListaDesdeRepositorio() {
        List<Categoria> listaMock = Arrays.asList(
                new Categoria(1L, "Fragancias"),
                new Categoria(2L, "Colonias")
        );

        when(categoriaRepository.findAll()).thenReturn(listaMock);

        List<Categoria> resultado = categoriaService.listarCategorias();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Fragancias");
    }

    @Test
    void obtenerPorId_deberiaRetornarCategoriaSiExiste() {
        Categoria categoria = new Categoria(1L, "Perfumes");

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        Optional<Categoria> resultado = categoriaService.obtenerPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("Perfumes");
    }   

    @Test
    void guardarCategoria_deberiaGuardarYRetornarCategoria() {
        Categoria categoria = new Categoria(null, "Aromas");

        when(categoriaRepository.save(categoria)).thenReturn(categoria);

        Categoria resultado = categoriaService.guardarCategoria(categoria);

        assertThat(resultado.getNombre()).isEqualTo("Aromas");
        verify(categoriaRepository).save(categoria);
    }

}
