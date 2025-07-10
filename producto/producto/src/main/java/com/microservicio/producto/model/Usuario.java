package com.microservicio.producto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Modelo de Usuario auxiliar para la autenticación y autorización")
public class Usuario {
    @Schema(description = "ID del usuario", example = "1")
    private Long id;
    @Schema(description = "Nickname del usuario", example = "usuario123")
    private String nickname;
    @Schema(description = "Correo electrónico del usuario", example = "usuario123@correo.com")
    private String correo;
    @Schema(description = "Clave del usuario", example = "claveSegura123")
    private String clave;
    @Schema(description = "Rol del usuario", example = "ADMIN")
    private Rol rol;

    public Usuario(Long id, String nickname, String correo, String clave, Rol rol) {
        this.id = id;
        this.nickname = nickname;
        this.correo = correo;
        this.clave = clave;
        this.rol = rol;
    }

}
