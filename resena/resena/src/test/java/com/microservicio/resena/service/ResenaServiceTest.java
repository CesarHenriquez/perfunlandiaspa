package com.microservicio.resena.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

import com.microservicio.resena.client.ProductoClient;
import com.microservicio.resena.client.UsuarioClient;
import com.microservicio.resena.model.Producto;
import com.microservicio.resena.model.Resena;
import com.microservicio.resena.model.Rol;
import com.microservicio.resena.model.Usuario;
import com.microservicio.resena.repository.ResenaRepository;

import reactor.core.publisher.Mono;


@ExtendWith(MockitoExtension.class)
public class ResenaServiceTest {
    @Mock
    private ResenaRepository resenaRepository;

    @Mock
    private ProductoClient productoClient;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private ResenaService resenaService;

    @Test
    void guardarResenaDesdePayload_usuarioClienteYProductoValidos_retornaCreated() {
        Map<String, Object> payload = Map.of(
                "nickname", "cliente1",
                "clave", "1234",
                "comentario", "Buen perfume",
                "puntuacion", 5,
                "productoId", 10L
        );

        Rol rol = new Rol();
        rol.setId(1L);
        rol.setNombre("CLIENTE");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNickname("cliente1");
        usuario.setClave("1234");
        usuario.setRol(rol);

        Producto producto = new Producto();
        producto.setId(10L);
        producto.setNombre("Perfume A");
        producto.setPrecio(49990.0);

        Resena resenaGuardada = new Resena(1L, "Buen perfume", 5, 10L, 1L);

        when(usuarioClient.obtenerUsuarioPorNickname("cliente1")).thenReturn(Mono.just(usuario));
        when(usuarioClient.validarClave("1234", "1234")).thenReturn(true);
        when(productoClient.obtenerProductoPorId(10L)).thenReturn(Mono.just(producto));
        when(resenaRepository.save(any(Resena.class))).thenReturn(resenaGuardada);

        ResponseEntity<?> respuesta = resenaService.guardarResenaDesdePayload(payload);

        assertThat(respuesta.getStatusCodeValue()).isEqualTo(201);
        assertThat(respuesta.getBody()).isInstanceOf(Resena.class);
        assertThat(((Resena) respuesta.getBody()).getComentario()).isEqualTo("Buen perfume");
    }

    @Test
    void listarResenas_deberiaRetornarTodasLasResenas() {
        Resena r1 = new Resena(1L, "Muy bueno", 5, 1L, 2L);
        Resena r2 = new Resena(2L, "No me gustó", 2, 2L, 2L);

        when(resenaRepository.findAll()).thenReturn(List.of(r1, r2));

        List<Resena> resultado = resenaService.listarResenas();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getComentario()).isEqualTo("Muy bueno");
    }

    @Test
    void listarPorProductoId_deberiaRetornarResenasDelProducto() {
        Resena r1 = new Resena(1L, "Muy bueno", 5, 2L, 2L);

        when(resenaRepository.findByProductoId(2L)).thenReturn(List.of(r1));

        List<Resena> resultado = resenaService.listarPorProductoId(2L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getProductoId()).isEqualTo(2L);
    }

    @Test
    void listarPorUsuarioId_deberiaRetornarResenasDelUsuario() {
        Resena r1 = new Resena(1L, "Excelente", 5, 3L, 4L);

        when(resenaRepository.findByUsuarioId(4L)).thenReturn(List.of(r1));

        List<Resena> resultado = resenaService.listarPorUsuarioId(4L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getUsuarioId()).isEqualTo(4L);
    }

}
