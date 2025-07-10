package com.microservicio.autenticacion.controller;

import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.http.MediaType;

import com.microservicio.autenticacion.service.AuthService;

import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(AuthController.class)
@Import(AuthControllerTest.SecurityDisabledConfig.class) // Desactiva seguridad en test
public class AuthControllerTest {

     @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AuthService authService;

    @Test
    public void login_retornaAutenticacionExitosa() {
        Map<String, String> credenciales = new HashMap<>();
        credenciales.put("nickname", "admin1");
        credenciales.put("clave", "clave1234");

        when(authService.autenticar("admin1", "clave1234"))
                .thenReturn(Mono.just("Autenticación exitosa. Rol: ADMIN"));

        webTestClient.post()
                .uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(credenciales)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Autenticación exitosa. Rol: ADMIN");
    }

    @Test
    public void login_retornaErrorSiCredencialesInvalidas() {
        Map<String, String> credenciales = new HashMap<>();
        credenciales.put("nickname", "gerente5");
        credenciales.put("clave", "incorrecta");

        when(authService.autenticar("gerente5", "incorrecta"))
                .thenReturn(Mono.just("Credenciales inválidas."));

        webTestClient.post()
                .uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(credenciales)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Credenciales inválidas.");
    }

    @TestConfiguration
    public static class SecurityDisabledConfig {
        @Bean
        public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
            return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                       .authorizeExchange(ex -> ex.anyExchange().permitAll())
                       .build();
        }
    }
}
