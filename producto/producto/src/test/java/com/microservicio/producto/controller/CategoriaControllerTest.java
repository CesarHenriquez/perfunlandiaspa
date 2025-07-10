package com.microservicio.producto.controller;

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
import com.microservicio.producto.model.Categoria;
import com.microservicio.producto.service.CategoriaService;

@WebMvcTest(CategoriaController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CategoriaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoriaService categoriaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarCategorias_retornaListaConStatusOk() throws Exception {
        Categoria categoria = new Categoria(1L, "Perfumes");

        when(categoriaService.listarCategorias()).thenReturn(List.of(categoria));

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Perfumes"));
    }

    @Test
    void crearCategoria_retornaCategoriaGuardadaConStatusOk() throws Exception {
        Categoria nuevaCategoria = new Categoria(null, "Dulce");
        Categoria guardada = new Categoria(2L, "Dulce");

        when(categoriaService.guardarCategoria(nuevaCategoria)).thenReturn(guardada);

        mockMvc.perform(post("/api/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevaCategoria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nombre").value("Dulce"));
    }

}
