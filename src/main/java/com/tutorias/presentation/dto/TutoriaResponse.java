package com.tutorias.presentation.dto;

import com.tutorias.domain.enums.EstadoTutoria;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TutoriaResponse {
    private String id;
    private String estudianteId;
    private String estudianteNombre;
    private String docenteId;
    private String docenteNombre;
    private String materiaId;
    private String materiaNombre;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String motivo;
    private EstadoTutoria estado;
}
