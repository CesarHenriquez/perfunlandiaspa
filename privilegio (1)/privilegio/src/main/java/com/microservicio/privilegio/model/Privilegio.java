package com.microservicio.privilegio.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "privilegio")
@Schema(description = "Modelo de Privilegio")
public class Privilegio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "privilegio_id")
    @Schema(description = "ID del privilegio", example = "1")
    private Long id;

    @Column(name = "rol", nullable = false, length = 50)
    @Schema(description = "Rol del privilegio", example = "ADMIN")
    private String rol;

    @Column(name = "descripcion", nullable = false, length = 255)
    @Schema(description = "Descripción del privilegio", example = "Acceso total al sistema")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "modulo_id", nullable = false)
    @JsonBackReference
    @Schema(description = "Módulo al que pertenece el privilegio")
    private Modulo modulo;

}
