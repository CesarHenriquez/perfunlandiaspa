package com.microservicio.producto.model;

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
@Table(name = "producto")
@Schema(description = "Modelo de Producto para la gestión de productos en el sistema")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producto_id")
    @Schema(description = "ID del producto", example = "1")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    @Schema(description = "Nombre del producto", example = "Perfume de lujo")
    private String nombre;

    @Column(name = "descripcion", nullable = false)
    @Schema(description = "Descripción del producto", example = "Perfume de alta calidad")
    private String descripcion;

    @Column(name = "precio", nullable = false)
    @Schema(description = "Precio del producto", example = "49.99")
    private Double precio;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    @Schema(description = "Categoría del producto")
    private Categoria categoria;

}
