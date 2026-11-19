package com.senati.turnobanco.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorDetailDTO {
    private String campo;
    private String mensaje;
}

