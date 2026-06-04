package com.tutorias.presentation.dto.request;

public record ActualizarEstadoTutoriaRequest(
    String nuevoEstado,
    String observacion,
    String usuarioResponsable
) {}
