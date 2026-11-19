package com.senati.turnobanco.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SingleResponseDTO<T> {
    private String status;
    private String fecha_respuesta;
    private T data;

    public static <T> SingleResponseDTO<T> success(T data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return SingleResponseDTO.<T>builder()
                .status("success")
                .fecha_respuesta(LocalDateTime.now().format(formatter))
                .data(data)
                .build();
    }
}

