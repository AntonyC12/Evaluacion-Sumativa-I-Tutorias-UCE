package com.tutorias.presentation.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionResponse {
    private String id;
    private String usuarioId;
    private String mensaje;
    private LocalDateTime fechaCreacion;
    private boolean leido;
}
