package com.microservicio.privilegio.dto;

import lombok.Data;

@Data
public class PrivilegioDTO {
    private String nickname;
    private String clave;
    private String rol;
    private String descripcion;
    private Long moduloId;

}
