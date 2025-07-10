package com.microservicio.privilegio.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Modelo de Rol auxiliar")
public class Rol {
    @Schema(description = "ID del rol", example = "4")
    private Long id;
    @Schema(description = "Nombre del rol", example = " ADMIN")
    private String nombre;


}
