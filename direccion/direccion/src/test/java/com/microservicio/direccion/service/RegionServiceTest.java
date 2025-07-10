package com.microservicio.direccion.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

import com.microservicio.direccion.model.Region;
import com.microservicio.direccion.repository.RegionRepository;

@ExtendWith(MockitoExtension.class)
public class RegionServiceTest {
      @Mock
    private RegionRepository regionRepository;

    @InjectMocks
    private RegionService regionService;

    @Test
    void listar_deberiaRetornarListaDeRegiones() {
        Region region1 = new Region(1L, "Metropolitana", null);
        Region region2 = new Region(2L, "Valparaíso", null);

        when(regionRepository.findAll()).thenReturn(Arrays.asList(region1, region2));

        List<Region> resultado = regionService.listar();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Metropolitana");
    }

    @Test
    void guardar_deberiaGuardarYRetornarRegion() {
        Region region = new Region(null, "Biobío", null);
        Region regionGuardada = new Region(3L, "Biobío", null);

        when(regionRepository.save(any(Region.class))).thenReturn(regionGuardada);

        Region resultado = regionService.guardar(region);

        assertThat(resultado.getId()).isEqualTo(3L);
        assertThat(resultado.getNombre()).isEqualTo("Biobío");
    }
     

}
