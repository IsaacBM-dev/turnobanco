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
public class ListResponseDTO<T> {
    private String status;
    private String fecha_respuesta;
    private List<T> data;
    private MetaDTO meta;

    public static <T> ListResponseDTO<T> success(List<T> data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return ListResponseDTO.<T>builder()
                .status("success")
                .fecha_respuesta(LocalDateTime.now().format(formatter))
                .data(data)
                .meta(new MetaDTO(data != null ? data.size() : 0))
                .build();
    }
}
