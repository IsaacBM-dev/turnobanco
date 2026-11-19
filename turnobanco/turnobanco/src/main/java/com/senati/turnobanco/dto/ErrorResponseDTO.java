package com.senati.turnobanco.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponseDTO {
    private String status;
    private String fecha_respuesta;
    private ErrorBodyDTO error;

    public static ErrorResponseDTO of(String codigo, String mensaje, List<ErrorDetailDTO> detalles) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return ErrorResponseDTO.builder()
                .status("error")
                .fecha_respuesta(LocalDateTime.now().format(formatter))
                .error(ErrorBodyDTO.builder()
                        .codigo(codigo)
                        .mensaje(mensaje)
                        .detalles(detalles)
                        .build())
                .build();
    }
}
