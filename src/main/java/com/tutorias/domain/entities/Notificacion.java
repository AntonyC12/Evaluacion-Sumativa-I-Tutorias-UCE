package com.tutorias.domain.entities;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notificacion {
    private String id;
    private String usuarioId;
    private String mensaje;
    private LocalDateTime fechaCreacion;
    private boolean leido;
}
