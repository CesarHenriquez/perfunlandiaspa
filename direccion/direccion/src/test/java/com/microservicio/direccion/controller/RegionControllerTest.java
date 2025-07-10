package com.microservicio.direccion.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicio.direccion.model.Region;
import com.microservicio.direccion.service.RegionService;


@WebMvcTest(RegionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RegionControllerTest {
       @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegionService regionService;

    @Autowired
    private ObjectMapper objectMapper;

    Region region = new Region(1L, "Metropolitana", null);

    @Test
    void listarRegiones_retornaListaConStatusOk() throws Exception {
        when(regionService.listar()).thenReturn(List.of(region));

        mockMvc.perform(get("/api/regiones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Metropolitana"));
    }

    @Test
    void guardarRegion_retornaRegionConStatusOk() throws Exception {
        when(regionService.guardar(region)).thenReturn(region);

        mockMvc.perform(post("/api/regiones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(region)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Metropolitana"));
    }

}
