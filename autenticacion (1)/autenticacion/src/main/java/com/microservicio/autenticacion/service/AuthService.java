package com.microservicio.autenticacion.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.microservicio.autenticacion.client.UsuarioClient;

import reactor.core.publisher.Mono;

@Service
public class AuthService {
    private final UsuarioClient usuarioClient;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioClient usuarioClient, PasswordEncoder passwordEncoder) {
        this.usuarioClient = usuarioClient;
        this.passwordEncoder = passwordEncoder;
    }

    public Mono<String> autenticar(String nickname, String clave) {
        return usuarioClient.obtenerUsuarioPorNickname(nickname)
                .flatMap(usuario -> {
                    if (usuario.getRol() == null) {
                        return Mono.just("Error: el usuario no tiene rol asignado.");
                    }
                    if (clave == null || usuario.getClave() == null) {
                        return Mono.just("Error: falta la clave.");
                    }
                    if (passwordEncoder.matches(clave, usuario.getClave())) {
                        return Mono.just("Autenticación exitosa. Rol: " + usuario.getRol().getNombre());
                    } else {
                        return Mono.just("Credenciales inválidas.");
                    }
                })
                .defaultIfEmpty("Usuario no encontrado.")
                .onErrorResume(e -> Mono.just("Error al autenticar: " + e.getMessage()));
    }

}
