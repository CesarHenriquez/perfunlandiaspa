package com.microservicio.privilegio.dto;

public class PrivilegioResumenDTO {
    private Long id;
    private String rol;
    private String descripcion;

    public PrivilegioResumenDTO(Long id, String rol, String descripcion) {
        this.id = id;
        this.rol = rol;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public String getRol() {
        return rol;
    }

    public String getDescripcion() {
        return descripcion;
    }

}
