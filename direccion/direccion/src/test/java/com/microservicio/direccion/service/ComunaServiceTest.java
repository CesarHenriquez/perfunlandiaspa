package com.microservicio.direccion.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.microservicio.direccion.model.Comuna;
import com.microservicio.direccion.model.Region;
import com.microservicio.direccion.repository.ComunaRepository;

@ExtendWith(MockitoExtension.class)
public class ComunaServiceTest {
     @Mock
    private ComunaRepository comunaRepository;

    @InjectMocks
    private ComunaService comunaService;

    @Test
    void listar_deberiaRetornarTodasLasComunas() {
        Region region = new Region(1L, "Metropolitana", null);
        List<Comuna> comunas = Arrays.asList(
                new Comuna(1L, "Santiago", region),
                new Comuna(2L, "Providencia", region)
        );

        when(comunaRepository.findAll()).thenReturn(comunas);

        List<Comuna> resultado = comunaService.listar();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Santiago");
    }

    @Test
    void listarPorRegion_deberiaRetornarComunasDeUnaRegion() {
        Region region = new Region(2L, "Valparaíso", null);
        List<Comuna> comunas = Arrays.asList(
                new Comuna(3L, "Viña del Mar", region),
                new Comuna(4L, "Valparaíso", region)
        );

        when(comunaRepository.findByRegionId(2L)).thenReturn(comunas);

        List<Comuna> resultado = comunaService.listarPorRegion(2L);

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(1).getNombre()).isEqualTo("Valparaíso");
    }

    @Test
    void guardar_deberiaGuardarYRetornarComuna() {
        Region region = new Region(1L, "Metropolitana", null);
        Comuna comuna = new Comuna(null, "La Florida", region);
        Comuna comunaGuardada = new Comuna(5L, "La Florida", region);

        when(comunaRepository.save(any(Comuna.class))).thenReturn(comunaGuardada);

        Comuna resultado = comunaService.guardar(comuna);

        assertThat(resultado.getId()).isEqualTo(5L);
        assertThat(resultado.getNombre()).isEqualTo("La Florida");
    }

}
