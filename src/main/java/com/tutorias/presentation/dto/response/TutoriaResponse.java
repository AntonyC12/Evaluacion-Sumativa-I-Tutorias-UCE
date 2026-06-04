package com.tutorias.presentation.dto.response;

public record TutoriaResponse(
    String id,
    String estudianteId,
    String estudianteNombre,
    String docenteId,
    String docenteNombre,
    String materiaId,
    String materiaNombre,
    String disponibilidadId,
    String fechaHoraInicio,
    String fechaHoraFin,
    String motivo,
    String estado,
    String fechaCreacion,
    String fechaActualizacion,
    String observacion
) {}
