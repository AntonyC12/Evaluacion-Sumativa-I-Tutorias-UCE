package com.tutorias.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TutoriaRequest {
    @NotBlank(message = "El id del estudiante es obligatorio")
    private String estudianteId;
    @NotBlank(message = "El id del docente es obligatorio")
    private String docenteId;
    @NotBlank(message = "El id de la materia es obligatorio")
    private String materiaId;
    @NotBlank(message = "El id de la disponibilidad es obligatorio")
    private String disponibilidadId;
    @NotBlank(message = "El motivo es obligatorio")
    private String motivo;
}
