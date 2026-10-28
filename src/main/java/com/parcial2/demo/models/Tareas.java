
package com.parcial2.demo.models;

import com.parcial2.demo.models.enums.Estado;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "tareas")
public class Tareas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTareas;

    @Column(nullable = false)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado = Estado.PENDIENTE;

    @ManyToOne
    @JoinColumn(name = "proyecto_id", referencedColumnName = "idproyectos", nullable = false)
    private Proyectos proyecto;

    @Column(nullable = false)
    private Boolean activo = true;
}
