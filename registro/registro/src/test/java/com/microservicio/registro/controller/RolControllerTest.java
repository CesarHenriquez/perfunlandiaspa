package com.microservicio.registro.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicio.registro.model.Rol;
import com.microservicio.registro.service.RolService;

@WebMvcTest(RolController.class)

@AutoConfigureMockMvc(addFilters = false) 
public class RolControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RolService rolService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void listarRoles_retornaOkYJson() throws Exception {
        Rol rol1 = new Rol(1L, "ADMIN");
        Rol rol2 = new Rol(2L, "CLIENTE");

        List<Rol> roles = Arrays.asList(rol1, rol2);
        when(rolService.listarRoles()).thenReturn(roles);

        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.rolList[0].id").value(1L))
                .andExpect(jsonPath("$._embedded.rolList[0].nombre").value("ADMIN"))
                .andExpect(jsonPath("$._embedded.rolList[1].id").value(2L))
                .andExpect(jsonPath("$._embedded.rolList[1].nombre").value("CLIENTE"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void obtenerRolPorId_retornaOkYJson() throws Exception {
        Rol rol = new Rol(1L, "ADMIN");
        when(rolService.obtenerPorId(1L)).thenReturn(Optional.of(rol));

        mockMvc.perform(get("/api/roles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("ADMIN"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void crearRol_retornaOkYJson() throws Exception {
        Rol nuevoRol = new Rol(null, "GERENTE");
        Rol rolGuardado = new Rol(3L, "GERENTE");

        when(rolService.guardarRol(nuevoRol)).thenReturn(rolGuardado);

        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoRol)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.nombre").value("GERENTE"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }
    

}
