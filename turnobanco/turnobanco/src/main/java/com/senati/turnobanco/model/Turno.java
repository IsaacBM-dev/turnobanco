package com.senati.turnobanco.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "turnos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_turno")
    private Integer idTurno;

    @Column(name = "codigo_turno", nullable = false, unique = true, length = 20)
    private String codigoTurno;

    @Column(name = "dni", nullable = false, length = 8)
    private String dni;

    @Column(name = "nombres", nullable = false, length = 80)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 80)
    private String apellidos;

    @Column(name = "numero_operaciones", nullable = false)
    private Integer numeroOperaciones;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado; // EN ESPERA, EN ATENCION, ATENDIDO

    // Relación Muchos a Uno con la tabla Tramites
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tramite", nullable = false)
    private Tramite tramite;

    // Cálculo 1: Tiempo estimado que durará la atención del cliente
    @Column(name = "tiempo_estimado_atencion_min", nullable = false, precision = 6, scale = 2)
    private BigDecimal tiempoEstimadoAtencionMin;

    // Cálculo 2: Tiempo estimado de espera en la cola antes de ser atendido
    @Column(name = "tiempo_espera_cola_min", nullable = false, precision = 6, scale = 2)
    private BigDecimal tiempoEsperaColaMin;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist
    public void prePersist() {
        if (this.fechaRegistro == null) {
            this.fechaRegistro = LocalDateTime.now();
        }
    }
}

