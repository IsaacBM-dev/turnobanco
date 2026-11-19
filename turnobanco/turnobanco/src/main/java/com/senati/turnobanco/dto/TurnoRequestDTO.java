package com.senati.turnobanco.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TurnoRequestDTO {

    @NotBlank(message = "El código de turno no puede estar vacío (Ej: V-005)")
    @Pattern(regexp = "^[A-Z]-[0-9]{3,4}$", message = "El código de turno debe tener el formato LETRA-NUMERO (Ej: V-001, P-002, A-003)")
    private String codigoTurno;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "^[0-9]{8}$", message = "El DNI debe contener exactamente 8 dígitos numéricos")
    private String dni;

    @NotBlank(message = "El nombre del cliente no puede estar vacío")
    @Size(min = 2, max = 80, message = "El nombre debe tener entre 2 y 80 caracteres")
    private String nombres;

    @NotBlank(message = "Los apellidos del cliente no pueden estar vacíos")
    @Size(min = 2, max = 80, message = "Los apellidos deben tener entre 2 y 80 caracteres")
    private String apellidos;

    @NotNull(message = "Debe indicar el número de operaciones a realizar")
    @Min(value = 1, message = "El número de operaciones debe ser como mínimo 1")
    @Max(value = 10, message = "El número máximo de operaciones por turno es 10")
    private Integer numeroOperaciones;

    @NotBlank(message = "El estado del turno es obligatorio")
    @Pattern(regexp = "^(EN ESPERA|EN ATENCION|ATENDIDO)$", message = "El estado debe ser: EN ESPERA, EN ATENCION o ATENDIDO")
    private String estado;

    @NotNull(message = "El ID del trámite es obligatorio (1: Ventanilla, 2: Plataforma, 3: Preferencial)")
    @Min(value = 1, message = "El ID del trámite debe ser un número positivo válido")
    private Integer idTramite;
}

