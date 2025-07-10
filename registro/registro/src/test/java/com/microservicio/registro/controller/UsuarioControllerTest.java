package com.microservicio.registro.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

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
import com.microservicio.registro.model.Usuario;
import com.microservicio.registro.service.UsuarioService;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false) 

public class UsuarioControllerTest {
   
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    Rol rol = new Rol(4L, "ADMIN");
    Usuario usuario = new Usuario(1L, "admin", "1234", "admin@correo.com", rol);

    @Test
    @WithMockUser(roles = "ADMIN")
    void registrarUsuario_retornaOkYJson() throws Exception {
        when(usuarioService.crearUsuario(usuario)).thenReturn(usuario);

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("admin"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listarUsuarios_retornaOkYJson() throws Exception {
        when(usuarioService.listarUsuarios()).thenReturn(List.of(usuario));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.usuarioList[0].nickname").value("admin"))
                .andExpect(jsonPath("$._embedded.usuarioList[0]._links.self.href").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void actualizarUsuario_retornaOkYJson() throws Exception {
        when(usuarioService.actualizarUsuario(1L, usuario)).thenReturn(usuario);

        mockMvc.perform(put("/api/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correo").value("admin@correo.com"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void eliminarUsuario_retornaOk() throws Exception {
        doNothing().when(usuarioService).eliminarUsuario(1L);

        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    void obtenerUsuarioPorId_retornaOkYJson() throws Exception {
        when(usuarioService.obtenerPorId(1L)).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @WithMockUser(roles = "LOGISTICA")
    void buscarPorNickname_retornaOkYJson() throws Exception {
        when(usuarioService.buscarPorNickname("admin")).thenReturn(usuario);

        mockMvc.perform(get("/api/usuarios/nickname/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correo").value("admin@correo.com"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }
   
    
}
