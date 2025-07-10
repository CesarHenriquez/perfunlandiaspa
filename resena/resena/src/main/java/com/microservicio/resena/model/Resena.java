package com.microservicio.resena.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "resena")
@Schema(description = "Modelo de Reseña de Producto")
public class Resena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resena_id")
    @Schema(description = "ID de la reseña", example = "1")
    private Long id;

    @Column(name = "comentario", nullable = false, length = 500)
    @Schema(description = "Comentario de la reseña", example = "Excelente producto, muy recomendable")
    private String comentario;

    @Column(name = "calificacion", nullable = false)
    @Schema(description = "Calificación del producto", example = "5")
    private int calificacion;

    @Column(name = "producto_id", nullable = false)
    @Schema(description = "ID del producto asociado a la reseña", example = "123")
    private Long productoId; 

    @Column(name = "usuario_id", nullable = false)
    @Schema(description = "ID del usuario que realizó la reseña", example = "456")
    private Long usuarioId; 


}
