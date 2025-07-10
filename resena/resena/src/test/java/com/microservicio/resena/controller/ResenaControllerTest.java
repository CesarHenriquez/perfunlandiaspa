package com.microservicio.resena.controller;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicio.resena.model.Resena;
import com.microservicio.resena.service.ResenaService;

@WebMvcTest(ResenaController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ResenaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResenaService resenaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarTodas_retornaListaConStatusOk() throws Exception {
        Resena resena = new Resena(1L, "Excelente producto", 5, 1L, 1L);

        when(resenaService.listarResenas()).thenReturn(List.of(resena));

        mockMvc.perform(get("/api/resenas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].comentario").value("Excelente producto"))
                .andExpect(jsonPath("$[0].calificacion").value(5));
    }

    @Test
    void listarPorProducto_retornaListaFiltradaPorProducto() throws Exception {
        Resena resena = new Resena(1L, "Me gustó mucho", 4, 2L, 1L);

        when(resenaService.listarPorProductoId(2L)).thenReturn(List.of(resena));

        mockMvc.perform(get("/api/resenas/producto/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productoId").value(2));
    }

    @Test
    void listarPorUsuario_retornaListaFiltradaPorUsuario() throws Exception {
        Resena resena = new Resena(1L, "Buen aroma", 5, 3L, 4L);

        when(resenaService.listarPorUsuarioId(4L)).thenReturn(List.of(resena));

        mockMvc.perform(get("/api/resenas/usuario/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuarioId").value(4));
    }

    @SuppressWarnings("unchecked")
    @Test
    void crearResena_valida_retornaCreated() throws Exception {
        Map<String, Object> payload = Map.of(
                "nickname", "cliente1",
                "clave", "1234",
                "comentario", "Muy bueno",
                "puntuacion", 5,
                "productoId", 1);

        Resena resenaGuardada = new Resena(1L, "Muy bueno", 5, 1L, 2L);

        when(resenaService.guardarResenaDesdePayload(anyMap()))
                .thenReturn((ResponseEntity) ResponseEntity.status(201).body(resenaGuardada));

        mockMvc.perform(post("/api/resenas")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comentario").value("Muy bueno"))
                .andExpect(jsonPath("$.calificacion").value(5));
    }

}
