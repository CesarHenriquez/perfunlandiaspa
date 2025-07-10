package com.microservicio.autenticacion.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.microservicio.autenticacion.client.UsuarioClient;
import com.microservicio.autenticacion.model.Rol;
import com.microservicio.autenticacion.model.Usuario;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void autenticar_retornaRolCuandoCredencialesSonValidas() {
        Rol rol = new Rol(4L, "ADMIN");
        Usuario usuario = new Usuario(1L, "admin", "1234hashed", "admin@mail.com", rol);

        when(usuarioClient.obtenerUsuarioPorNickname("admin")).thenReturn(Mono.just(usuario));
        when(passwordEncoder.matches("1234", "1234hashed")).thenReturn(true);

        String resultado = authService.autenticar("admin", "1234").block();
        assertEquals("Autenticación exitosa. Rol: ADMIN", resultado);
    }

    @Test
    void autenticar_retornaErrorCuandoClaveIncorrecta() {
        Rol rol = new Rol(2L, "GERENTE");
        Usuario usuario = new Usuario(2L, "gerente1", "clavehashed", "gerente@mail.com", rol);

      
        when(usuarioClient.obtenerUsuarioPorNickname("gerente1")).thenReturn(Mono.just(usuario));
        when(passwordEncoder.matches("claveIncorrecta", "clavehashed")).thenReturn(false);

        String resultado = authService.autenticar("gerente1", "claveIncorrecta").block();
        assertEquals("Credenciales inválidas.", resultado);
    }

    @Test
    void autenticar_retornaErrorSiNoExisteUsuario() {
        when(usuarioClient.obtenerUsuarioPorNickname("desconocido")).thenReturn(Mono.empty());

        String resultado = authService.autenticar("desconocido", "cualquiera").block();
        assertEquals("Usuario no encontrado.", resultado);
    }

}
