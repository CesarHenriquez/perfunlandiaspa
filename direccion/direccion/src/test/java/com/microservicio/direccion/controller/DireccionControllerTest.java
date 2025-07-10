package com.microservicio.direccion.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicio.direccion.client.UsuarioClient;
import com.microservicio.direccion.model.Comuna;
import com.microservicio.direccion.model.Direccion;
import com.microservicio.direccion.model.Region;
import com.microservicio.direccion.model.Rol;
import com.microservicio.direccion.model.Usuario;
import com.microservicio.direccion.service.DireccionService;

import reactor.core.publisher.Mono;

@WebMvcTest(DireccionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class DireccionControllerTest {
     @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DireccionService direccionService;

    @MockBean
    private UsuarioClient usuarioClient;

    @Autowired
    private ObjectMapper objectMapper;

    Region region = new Region(1L, "Metropolitana", List.of());
    Comuna comuna = new Comuna(1L, "Providencia", region);
    Direccion direccion = new Direccion(1L, "Los Leones", "1234567", 1L, comuna);
    Rol rolCliente = new Rol(1L, "CLIENTE");
    Usuario usuarioCliente = new Usuario(1L, "cliente1", "1234", "cliente@correo.com", rolCliente);

    @Test
    void listarDirecciones_retornaListaConStatusOk() throws Exception {
        when(direccionService.listar()).thenReturn(List.of(direccion));

        mockMvc.perform(get("/api/direcciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].calle").value("Los Leones"));
    }

    @Test
    void obtenerPorUsuarioId_retornaListaConStatusOk() throws Exception {
        when(direccionService.buscarPorUsuario(1L)).thenReturn(List.of(direccion));

        mockMvc.perform(get("/api/direcciones/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigoPostal").value("1234567"));
    }

    @Test
    void guardarDireccion_conRolCliente_retornaCreado() throws Exception {
        Map<String, Object> payload = Map.of(
                "nickname", "cliente1",
                "clave", "1234",
                "calle", "Los Leones",
                "codigoPostal", "1234567",
                "comunaId", 1
        );

        when(usuarioClient.obtenerUsuarioPorNickname("cliente1")).thenReturn(Mono.just(usuarioCliente));
        when(usuarioClient.validarClave("1234", "1234")).thenReturn(true);
        when(direccionService.guardar(org.mockito.ArgumentMatchers.any(Direccion.class))).thenReturn(direccion);

        mockMvc.perform(post("/api/direcciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.calle").value("Los Leones"));
    }

    @Test
    void guardarDireccion_conRolNoCliente_retornaForbidden() throws Exception {
        Rol rolAdmin = new Rol(4L, "ADMIN");
        Usuario usuarioAdmin = new Usuario(2L, "admin", "1234", "admin@correo.com", rolAdmin);

        Map<String, Object> payload = Map.of(
                "nickname", "admin",
                "clave", "1234",
                "calle", "Otra Calle",
                "codigoPostal", "999999",
                "comunaId", 1
        );

        when(usuarioClient.obtenerUsuarioPorNickname("admin")).thenReturn(Mono.just(usuarioAdmin));
        when(usuarioClient.validarClave("1234", "1234")).thenReturn(true);

        mockMvc.perform(post("/api/direcciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Acceso denegado: solo usuarios con rol CLIENTE pueden registrar direcciones."));
    }
    
    

}
