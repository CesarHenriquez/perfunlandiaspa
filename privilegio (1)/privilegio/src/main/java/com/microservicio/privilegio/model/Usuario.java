package com.microservicio.privilegio.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Modelo de Usuario auxiliar")
public class Usuario {
    @Schema(description = "ID del usuario", example = "1")
    private Long id;
    @Schema(description = "Nombre del usuario", example = "Juan Perez")
    private String nickname;
    @Schema(description = "Clave del usuario", example = "clave123")
    private String clave;
    @Schema(description = "Correo electrónico del usuario", example = "juan1@correo.com")
    private String correo;
    @Schema(description = "Rol del usuario")
    private Rol rol;

}
