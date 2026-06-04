package com.tutorias.presentation.dto.response;

public record DisponibilidadResponse(
    String id,
    String docenteId,
    String docenteNombre,
    String materiaId,
    String materiaNombre,
    String fecha,
    String horaInicio,
    String horaFin,
    String estado
) {}
