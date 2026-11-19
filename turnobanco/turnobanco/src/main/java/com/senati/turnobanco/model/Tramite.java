package com.senati.turnobanco.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tramites")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tramite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tramite")
    private Integer idTramite;

    @Column(name = "nombre_tramite", nullable = false, unique = true, length = 60)
    private String nombreTramite; // VENTANILLA, PLATAFORMA, PREFERENCIAL

    @Column(name = "prefijo", nullable = false, length = 5)
    private String prefijo; // V, P, A

    @Column(name = "tiempo_base_min", nullable = false)
    private Integer tiempoBaseMin;

    @Column(name = "factor_prioridad", nullable = false, precision = 5, scale = 2)
    private BigDecimal factorPrioridad;

    @Column(name = "descripcion", nullable = false, length = 150)
    private String descripcion;
}
