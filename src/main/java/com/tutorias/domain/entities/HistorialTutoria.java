package com.tutorias.domain.entities;

import com.tutorias.domain.enums.EstadoTutoria;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistorialTutoria {
    private String id;
    private String tutoriaId;
    private EstadoTutoria estadoAnterior;
    private EstadoTutoria estadoNuevo;
    private LocalDateTime fechaCambio;
    private String modificadoPor;
    private String detalle;
}
