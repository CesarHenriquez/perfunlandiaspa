package com.microservicio.direccion.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservicio.direccion.client.UsuarioClient;
import com.microservicio.direccion.model.Comuna;
import com.microservicio.direccion.model.Direccion;
import com.microservicio.direccion.model.Usuario;
import com.microservicio.direccion.service.DireccionService;

@RestController
@RequestMapping("/api/direcciones")
public class DireccionController {
    private final DireccionService direccionService;
    private final UsuarioClient usuarioClient;

    public DireccionController(DireccionService direccionService, UsuarioClient usuarioClient) {
        this.direccionService = direccionService;
        this.usuarioClient = usuarioClient;
    }

    // Solo usuarios con rol LOGISTICA pueden listar todas las direcciones
    @GetMapping
    public List<Direccion> listar() {
        return direccionService.listar();
    }

    // Obtener direcciones por usuario ID (sin validación aquí)
    @GetMapping("/usuario/{usuarioId}")
    public List<Direccion> porUsuario(@PathVariable Long usuarioId) {
        return direccionService.buscarPorUsuario(usuarioId);
    }

    // Crear dirección solo si el usuario tiene rol CLIENTE
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Map<String, Object> payload) {
        try {
            String nickname = (String) payload.get("nickname");
            String clave = (String) payload.get("clave");

            Usuario usuario = usuarioClient.obtenerUsuarioPorNickname(nickname).block();

            if (usuario == null || usuario.getRol() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no válido o sin rol.");
            }

            if (!usuarioClient.validarClave(clave, usuario.getClave())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Clave incorrecta.");
            }

            if (!usuario.getRol().getNombre().equalsIgnoreCase("CLIENTE")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Acceso denegado: solo usuarios con rol CLIENTE pueden registrar direcciones.");
            }

            // Crear nueva dirección
            Direccion direccion = new Direccion();
            direccion.setCalle((String) payload.get("calle"));
            direccion.setCodigoPostal((String) payload.get("codigoPostal"));
            direccion.setUsuarioId(usuario.getId());

            Comuna comuna = new Comuna();
            comuna.setId(Long.valueOf(payload.get("comunaId").toString()));
            direccion.setComuna(comuna);

            Direccion guardada = direccionService.guardar(direccion);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardada);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al procesar la solicitud: " + e.getMessage());
        }
    }

}
