package com.microservicio.direccion.service;

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

import com.microservicio.direccion.model.Comuna;
import com.microservicio.direccion.model.Direccion;
import com.microservicio.direccion.repository.ComunaRepository;
import com.microservicio.direccion.repository.DireccionRepository;

@ExtendWith(MockitoExtension.class)
public class DireccionServiceTest {
    @Mock
    private DireccionRepository direccionRepository;

    @Mock
    private ComunaRepository comunaRepository;

    @InjectMocks
    private DireccionService direccionService;

    @Test
    void guardar_deberiaGuardarDireccionConComunaCompleta() {
        Comuna comuna = new Comuna(1L, "Santiago", null);
        Direccion direccionEntrada = new Direccion(null, "Av. Siempre Viva", "1234", 10L, new Comuna(1L, null, null));
        Direccion direccionEsperada = new Direccion(5L, "Av. Siempre Viva", "1234", 10L, comuna);

        when(comunaRepository.findById(1L)).thenReturn(Optional.of(comuna));
        when(direccionRepository.save(direccionEntrada)).thenReturn(direccionEsperada);

        Direccion resultado = direccionService.guardar(direccionEntrada);

        assertThat(resultado.getId()).isEqualTo(5L);
        assertThat(resultado.getCalle()).isEqualTo("Av. Siempre Viva");
        assertThat(resultado.getComuna().getNombre()).isEqualTo("Santiago");
    }

    @Test
    void listar_deberiaRetornarTodasLasDirecciones() {
        Comuna comuna = new Comuna(1L, "Maipú", null);
        List<Direccion> direcciones = Arrays.asList(
                new Direccion(1L, "Calle 1", "111", 5L, comuna),
                new Direccion(2L, "Calle 2", "222", 6L, comuna)
        );

        when(direccionRepository.findAll()).thenReturn(direcciones);

        List<Direccion> resultado = direccionService.listar();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(1).getCalle()).isEqualTo("Calle 2");
    }

    @Test
    void buscarPorUsuario_deberiaRetornarDireccionesDelUsuario() {
        Comuna comuna = new Comuna(2L, "La Reina", null);
        List<Direccion> direcciones = Arrays.asList(
                new Direccion(1L, "Calle A", "456", 99L, comuna)
        );

        when(direccionRepository.findByUsuarioId(99L)).thenReturn(direcciones);

        List<Direccion> resultado = direccionService.buscarPorUsuario(99L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCodigoPostal()).isEqualTo("456");
    }

}
