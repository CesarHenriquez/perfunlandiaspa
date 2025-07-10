package com.microservicio.producto.controller;

import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.MediaType;

import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicio.producto.client.UsuarioClient;
import com.microservicio.producto.model.Categoria;
import com.microservicio.producto.model.Producto;
import com.microservicio.producto.model.Rol;
import com.microservicio.producto.model.Usuario;
import com.microservicio.producto.service.ProductoService;

import reactor.core.publisher.Mono;

@WebMvcTest(ProductoController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @MockBean
    private UsuarioClient usuarioClient;

    @Autowired
    private ObjectMapper objectMapper;

    Rol rolAdmin = new Rol(4L, "ADMIN");
    Usuario admin = new Usuario(1L, "admin", "admin@correo.com", "1234", rolAdmin);
    Categoria categoria = new Categoria(1L, "Perfumes");
    Producto producto = new Producto(1L, "Perfume X", "Fragancia elegante", 19990.0, categoria);

    @Test
    void listarProductos_retornaOkYJson() throws Exception {
        Mockito.when(productoService.listarProductos()).thenReturn(List.of(producto));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Perfume X"));
    }

    @Test
    void obtenerProductoPorId_retornaOkSiExiste() throws Exception {
        Mockito.when(productoService.obtenerPorId(1L)).thenReturn(Optional.of(producto));

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descripcion").value("Fragancia elegante"));
    }

    @Test
    void crearProducto_conRolAdminYClaveCorrecta_retornaCreated() throws Exception {
        Mockito.when(usuarioClient.obtenerUsuarioPorNickname("admin")).thenReturn(Mono.just(admin));
        Mockito.when(usuarioClient.validarClave("1234", "1234")).thenReturn(true);
        Mockito.when(productoService.guardarProducto(any(Producto.class))).thenReturn(producto);

        Map<String, Object> payload = Map.of(
                "nickname", "admin",
                "clave", "1234",
                "nombre", "Perfume X",
                "descripcion", "Fragancia elegante",
                "precio", 19990.0,
                "categoriaId", 1
        );

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Perfume X"));
    }

    @Test
    void editarProducto_conRolAdmin_retornaOk() throws Exception {
        Mockito.when(usuarioClient.obtenerUsuarioPorNickname("admin")).thenReturn(Mono.just(admin));
        Mockito.when(usuarioClient.validarClave("1234", "1234")).thenReturn(true);
        Mockito.when(productoService.obtenerPorId(1L)).thenReturn(Optional.of(producto));
        Mockito.when(productoService.guardarProducto(any(Producto.class))).thenReturn(producto);

        Map<String, Object> payload = Map.of(
                "nickname", "admin",
                "clave", "1234",
                "nombre", "Perfume Editado",
                "descripcion", "Aroma suave",
                "precio", 17990.0,
                "categoriaId", 1
        );

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Perfume Editado"));

    }

    @Test
    void eliminarProducto_conRolAdmin_retornaNoContent() throws Exception {
        Mockito.when(usuarioClient.obtenerUsuarioPorNickname("admin")).thenReturn(Mono.just(admin));
        Mockito.when(usuarioClient.validarClave("1234", "1234")).thenReturn(true);

        mockMvc.perform(delete("/api/productos/1")
                        .param("nickname", "admin")
                        .param("clave", "1234"))
                .andExpect(status().isNoContent());
    }
   
    

}
