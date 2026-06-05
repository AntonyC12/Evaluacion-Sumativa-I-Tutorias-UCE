package com.tutorias.presentation.dto;

import com.tutorias.domain.enums.EstadoTutoria;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistorialTutoriaResponse {
    private String id;
    private String tutoriaId;
    private EstadoTutoria estadoAnterior;
    private EstadoTutoria estadoNuevo;
    private LocalDateTime fechaCambio;
    private String modificadoPor;
    private String detalle;
}
