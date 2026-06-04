package com.tutorias.presentation.dto.request;

public record CrearTutoriaRequest(
    String estudianteId,
    String disponibilidadId,
    String motivo
) {}
