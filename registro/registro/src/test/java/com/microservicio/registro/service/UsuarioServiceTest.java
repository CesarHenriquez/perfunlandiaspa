package com.microservicio.registro.service;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;

import com.microservicio.registro.model.Rol;
import com.microservicio.registro.model.Usuario;
import com.microservicio.registro.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void crearUsuario_deberiaGuardarUsuarioConClaveEncriptada() {
        Rol rol = new Rol(4L, "ADMIN");
        Usuario usuarioSinEncriptar = new Usuario(null, "admin1", "123", "admin1@correo.com", rol);
        String claveEncriptada = "claveEncriptada123";
        Usuario usuarioEncriptado = new Usuario(null, "admin1", claveEncriptada, "admin1@correo.com", rol);

        when(passwordEncoder.encode("123")).thenReturn(claveEncriptada);
        when(usuarioRepository.save(usuarioEncriptado)).thenReturn(usuarioEncriptado);

        Usuario resultado = usuarioService.crearUsuario(usuarioSinEncriptar);

        assertThat(resultado.getClave()).isEqualTo(claveEncriptada);
        assertThat(resultado.getNickname()).isEqualTo("admin1");
    }

    @Test
    void listarUsuarios_deberiaRetornarListaDesdeRepositorio() {
        Rol rol = new Rol(1L, "CLIENTE");
        List<Usuario> mockUsuarios = Arrays.asList(
                new Usuario(1L, "cliente1", "clave123", "cliente1@correo.com", rol));

        when(usuarioRepository.findAll()).thenReturn(mockUsuarios);

        List<Usuario> resultado = usuarioService.listarUsuarios();

        assertThat(resultado).isEqualTo(mockUsuarios);
    }

    @Test
    void buscarPorNickname_deberiaRetornarUsuarioSiExiste() {
        Rol rol = new Rol(4L, "ADMIN");
        Usuario mockUsuario = new Usuario(1L, "admin1", "claveEncriptada", "admin1@correo.com", rol);

        when(usuarioRepository.findByNickname("admin1")).thenReturn(Optional.of(mockUsuario));

        Usuario resultado = usuarioService.buscarPorNickname("admin1");

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNickname()).isEqualTo("admin1");
    }

    @Test
    void actualizarUsuario_deberiaActualizarCamposYGuardar() {
        Rol rol = new Rol(4L, "ADMIN");
        Usuario existente = new Usuario(1L, "admin1", "clave1234", "admin1x@correo.com", rol);
        Usuario actualizado = new Usuario(null, "adminNuevo", "claveNueva", "nuevo@correo.com", rol);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(passwordEncoder.encode("claveNueva")).thenReturn("claveNuevaEncriptada");
        when(usuarioRepository.save(existente)).thenReturn(existente);

        Usuario resultado = usuarioService.actualizarUsuario(1L, actualizado);

        assertThat(resultado.getNickname()).isEqualTo("adminNuevo");
        assertThat(resultado.getCorreo()).isEqualTo("nuevo@correo.com");
        assertThat(resultado.getClave()).isEqualTo("claveNuevaEncriptada");
    }

    @Test
    void eliminarUsuario_deberiaEliminarPorId() {
        Long idUsuario = 1L;

        
        usuarioService.eliminarUsuario(idUsuario);

      
        Mockito.verify(usuarioRepository).deleteById(idUsuario);
    }

}
