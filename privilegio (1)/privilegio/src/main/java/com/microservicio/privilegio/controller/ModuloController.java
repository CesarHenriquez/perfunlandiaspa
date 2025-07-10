package com.microservicio.privilegio.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.microservicio.privilegio.model.Modulo;
import com.microservicio.privilegio.service.ModuloService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import com.microservicio.privilegio.client.UsuarioClient;
import com.microservicio.privilegio.model.Usuario;

@RestController
@RequestMapping("/api/modulos")
public class ModuloController {

    private final ModuloService moduloService;
    private final UsuarioClient usuarioClient;

    public ModuloController(ModuloService moduloService, UsuarioClient usuarioClient) {
        this.moduloService = moduloService;
        this.usuarioClient = usuarioClient;
    }

    @Operation(summary = "Listar todos los módulos", description = "Obtiene una lista de todos los módulos disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de módulos obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public List<Modulo> listar() {
        return moduloService.listarModulos();
    }
    @Operation(summary = "Obtener un módulo por ID", description = "Obtiene un módulo específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Módulo obtenido correctamente"),
        @ApiResponse(responseCode = "404", description = "Módulo no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Map<String, Object> payload) {
        try {
            String nickname = (String) payload.get("nickname");
            String clave = (String) payload.get("clave");

            Usuario usuario = usuarioClient.obtenerUsuarioPorNickname(nickname).block();
            if (usuario == null || usuario.getRol() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario inválido.");
            }

            if (!usuarioClient.validarClave(clave, usuario.getClave())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Clave incorrecta.");
            }

            if (!usuario.getRol().getNombre().equalsIgnoreCase("ADMIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado.");
            }

            Modulo modulo = new Modulo();
            modulo.setNombre((String) payload.get("nombre"));

            return ResponseEntity.status(HttpStatus.CREATED).body(moduloService.guardarModulo(modulo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear módulo: " + e.getMessage());
        }
    }

    @Operation(summary = "Actualizar un módulo por ID", description = "Actualiza un módulo específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Módulo actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Módulo no encontrado"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        try {
            String nickname = (String) payload.get("nickname");
            String clave = (String) payload.get("clave");

            Usuario usuario = usuarioClient.obtenerUsuarioPorNickname(nickname).block();
            if (usuario == null || usuario.getRol() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario inválido.");
            }

            if (!usuarioClient.validarClave(clave, usuario.getClave())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Clave incorrecta.");
            }

            if (!usuario.getRol().getNombre().equalsIgnoreCase("ADMIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado.");
            }

            Modulo modulo = new Modulo();
            modulo.setNombre((String) payload.get("nombre"));

            return ResponseEntity.ok(moduloService.actualizarModulo(id, modulo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar módulo: " + e.getMessage());
        }
    }

    @Operation(summary = "Eliminar un módulo por ID", description = "Elimina un módulo específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Módulo eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Módulo no encontrado"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        try {
            String nickname = (String) payload.get("nickname");
            String clave = (String) payload.get("clave");

            Usuario usuario = usuarioClient.obtenerUsuarioPorNickname(nickname).block();
            if (usuario == null || usuario.getRol() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario inválido.");
            }

            if (!usuarioClient.validarClave(clave, usuario.getClave())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Clave incorrecta.");
            }

            if (!usuario.getRol().getNombre().equalsIgnoreCase("ADMIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado.");
            }

            moduloService.eliminarModulo(id);
            return ResponseEntity.ok("Módulo eliminado con éxito.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar módulo: " + e.getMessage());
        }
    }
}