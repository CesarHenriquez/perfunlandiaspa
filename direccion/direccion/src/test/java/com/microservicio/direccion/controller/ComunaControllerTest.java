package com.microservicio.direccion.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.microservicio.direccion.model.Comuna;
import com.microservicio.direccion.model.Region;
import com.microservicio.direccion.service.ComunaService;

@WebMvcTest(ComunaController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ComunaControllerTest {
     @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComunaService comunaService;

    Region region = new Region(1L, "Metropolitana", List.of());
    Comuna comuna = new Comuna(1L, "Providencia", region);

    @Test
    void listarComunas_retornaListaConStatusOk() throws Exception {
        when(comunaService.listar()).thenReturn(List.of(comuna));

        mockMvc.perform(get("/api/comunas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Providencia"));
    }

    @Test
    void listarComunasPorRegionId_retornaListaConStatusOk() throws Exception {
        when(comunaService.listarPorRegion(1L)).thenReturn(List.of(comuna));

        mockMvc.perform(get("/api/comunas/region/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].region.id").value(1));
    }

}
