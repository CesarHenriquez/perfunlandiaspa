package com.microservicio.privilegio.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservicio.privilegio.dto.PrivilegioDTO;
import com.microservicio.privilegio.dto.PrivilegioResumenDTO;

import com.microservicio.privilegio.service.PrivilegioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/privilegios")
public class PrivilegioController {
    private final PrivilegioService privilegioService;

    public PrivilegioController(PrivilegioService privilegioService) {
        this.privilegioService = privilegioService;
    }

    @Operation(summary = "Crear un nuevo privilegio", description = "Permite crear un nuevo privilegio en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Privilegio creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody PrivilegioDTO dto) {
        return privilegioService.crearPrivilegio(dto);
    }

    @Operation(summary = "Listar todos los privilegios", description = "Obtiene una lista de todos los privilegios disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de privilegios obtenida correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<PrivilegioResumenDTO>> listar() {
        return ResponseEntity.ok(privilegioService.listarPrivilegios());
    }

    @Operation(summary = "Obtener un privilegio por ID", description = "Obtiene un privilegio específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Privilegio obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Privilegio no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody PrivilegioDTO dto) {
        return privilegioService.actualizarPrivilegio(id, dto);
    }

    @Operation(summary = "Eliminar un privilegio por ID", description = "Elimina un privilegio específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Privilegio eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Privilegio no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id, @RequestParam String nickname) {
        return privilegioService.eliminarPrivilegio(id, nickname);
    }

}
