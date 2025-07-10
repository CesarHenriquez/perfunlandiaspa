package com.microservicio.resena.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Modelo de Rol de Usuario auxiliar")
public class Rol {
    @Schema(description = "ID del rol", example = "4")
    private Long id;
    @Schema(description = "Nombre del rol", example = "ADMIN")
    private String nombre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
