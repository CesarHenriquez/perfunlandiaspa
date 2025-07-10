package com.microservicio.resena.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservicio.resena.model.Resena;
import com.microservicio.resena.service.ResenaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/resenas")
public class ResenaController {
    private final ResenaService resenaService;

    public ResenaController(ResenaService resenaService) {
        this.resenaService = resenaService;
    }
    @Operation(summary = "Crear reseña", description = "Crea una nueva reseña de producto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reseña creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida, datos incorrectos"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado, se requiere autenticación")
    })

    @PostMapping
    public ResponseEntity<?> crearResena(@RequestBody Map<String, Object> payload) {
        return resenaService.guardarResenaDesdePayload(payload);
    }
    @Operation(summary = "Listar reseñas", description = "Obtiene una lista de todas las reseñas de productos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de reseñas obtenida exitosamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado, se requiere autenticación")
    })

  
    @GetMapping
    public List<Resena> listarTodas() {
        return resenaService.listarResenas();
    }
    @Operation(summary = "Listar reseñas por producto", description = "Obtiene una lista de reseñas asociadas a un producto específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de reseñas por producto obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado, se requiere autenticación")
    })

    @GetMapping("/producto/{productoId}")
    public List<Resena> listarPorProducto(@PathVariable Long productoId) {
        return resenaService.listarPorProductoId(productoId);
    }

    @Operation(summary = "Listar reseñas por usuario", description = "Obtiene una lista de reseñas asociadas a un usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de reseñas por usuario obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado, se requiere autenticación")
    })

    @GetMapping("/usuario/{usuarioId}")
    public List<Resena> listarPorUsuario(@PathVariable Long usuarioId) {
        return resenaService.listarPorUsuarioId(usuarioId);
    }

}
