package com.tutorias.presentation.dto.request;

public record CrearDisponibilidadRequest(
    String docenteId,
    String materiaId,
    String fecha,
    String horaInicio,
    String horaFin
) {}
