package com.senati.turnobanco.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorBodyDTO {
    private String codigo;
    private String mensaje;
    private List<ErrorDetailDTO> detalles;
}

