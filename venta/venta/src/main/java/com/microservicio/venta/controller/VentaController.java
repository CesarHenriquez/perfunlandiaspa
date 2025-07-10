package com.microservicio.venta.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservicio.venta.client.UsuarioClient;
import com.microservicio.venta.dto.ProductoDetalleDTO;
import com.microservicio.venta.model.DetalleVenta;
import com.microservicio.venta.model.Usuario;
import com.microservicio.venta.model.Venta;
import com.microservicio.venta.service.VentaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {
    private final VentaService ventaService;
    private final UsuarioClient usuarioClient;

    public VentaController(VentaService ventaService, UsuarioClient usuarioClient) {
        this.ventaService = ventaService;
        this.usuarioClient = usuarioClient;
    }

    @Operation(summary = "Registrar una nueva venta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venta registrada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

    @PostMapping
    public ResponseEntity<?> registrarVenta(@RequestBody Map<String, Object> payload) {
        return ventaService.registrarVentaDesdePayload(payload);
    }

    @Operation(summary = "Listar todas las ventas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ventas listadas exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron ventas"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

    @GetMapping
    public List<Venta> listarVentas() {
        return ventaService.listarVentas();
    }

    @Operation(summary = "Listar todos los detalles de ventas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalles de ventas listados exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron detalles de ventas"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

    @GetMapping("/detalles")
    public List<DetalleVenta> listarDetalles() {
        return ventaService.listarDetalles();
    }

    @Operation(summary = "Listar ventas por ID de usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ventas del usuario listadas exitosamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado, solo ADMIN puede ver ventas de otros usuarios"),
            @ApiResponse(responseCode = "404", description = "No se encontraron ventas para el usuario"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

    @GetMapping("/usuario/{id}")
    public ResponseEntity<?> listarPorUsuario(
            @PathVariable Long id,
            @RequestHeader("nickname") String nickname,
            @RequestHeader("clave") String clave) {

        try {
            Usuario usuario = usuarioClient.obtenerUsuarioPorNickname(nickname).block();
            if (usuario == null || usuario.getRol() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario inválido o sin rol.");
            }

            if (!usuarioClient.validarClave(clave, usuario.getClave())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Clave incorrecta.");
            }

            String rol = usuario.getRol().getNombre().toUpperCase();
            if (!rol.equals("ADMIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Solo el ADMIN puede ver las ventas de otros usuarios.");
            }

            List<Venta> ventas = ventaService.listarPorUsuarioId(id);
            if (ventas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontraron ventas para el usuario con ID: " + id);
            }
            return ResponseEntity.ok(ventas);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la solicitud: " + e.getMessage());
        }
    }

    @Operation(summary = "Listar ventas por ID de dirección")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ventas por dirección listadas exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron ventas para la dirección"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

    @GetMapping("/direccion/{id}")
    public List<Venta> listarPorDireccion(@PathVariable Long id) {
        return ventaService.listarPorDireccionId(id);
    }

    @Operation(summary = "Obtener productos de una venta por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos obtenidos exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron productos para la venta"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

    @GetMapping("/{id}/productos")
    public ResponseEntity<?> obtenerProductosDeVenta(@PathVariable Long id) {
        try {
            List<ProductoDetalleDTO> productos = ventaService.obtenerProductosPorVentaId(id);
            if (productos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontraron productos para la venta con ID: " + id);
            }
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener productos de la venta: " + e.getMessage());
        }
    }

}
