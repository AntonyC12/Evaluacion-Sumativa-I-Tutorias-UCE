package com.tutorias.presentation.mapper;

import com.tutorias.domain.model.Disponibilidad;
import com.tutorias.presentation.dto.response.DisponibilidadResponse;
import java.time.format.DateTimeFormatter;

public class DisponibilidadMapper {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static DisponibilidadResponse toResponse(Disponibilidad d) {
        if (d == null) return null;
        String fecha = d.getFecha() != null ? d.getFecha().format(DATE_FORMATTER) : null;
        String inicio = d.getHoraInicio() != null ? d.getHoraInicio().format(TIME_FORMATTER) : null;
        String fin = d.getHoraFin() != null ? d.getHoraFin().format(TIME_FORMATTER) : null;

        return new DisponibilidadResponse(
            d.getId(),
            d.getDocenteId(),
            d.getDocenteNombre(),
            d.getMateriaId(),
            d.getMateriaNombre(),
            fecha,
            inicio,
            fin,
            d.getEstado()
        );
    }
}
