package com.tutorias.presentation.mapper;

import com.tutorias.domain.model.Tutoria;
import com.tutorias.presentation.dto.response.TutoriaResponse;
import java.time.format.DateTimeFormatter;

public class TutoriaMapper {
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static TutoriaResponse toResponse(Tutoria t) {
        if (t == null) return null;
        String inicio = t.getFechaHoraInicio() != null ? t.getFechaHoraInicio().format(DATETIME_FORMATTER) : null;
        String fin = t.getFechaHoraFin() != null ? t.getFechaHoraFin().format(DATETIME_FORMATTER) : null;
        String creacion = t.getFechaCreacion() != null ? t.getFechaCreacion().format(DATETIME_FORMATTER) : null;
        String actualizacion = t.getFechaActualizacion() != null ? t.getFechaActualizacion().format(DATETIME_FORMATTER) : null;

        return new TutoriaResponse(
            t.getId(),
            t.getEstudianteId(),
            t.getEstudianteNombre(),
            t.getDocenteId(),
            t.getDocenteNombre(),
            t.getMateriaId(),
            t.getMateriaNombre(),
            t.getDisponibilidadId(),
            inicio,
            fin,
            t.getMotivo().value(),
            t.getEstado().getValor(),
            creacion,
            actualizacion,
            t.getObservacion()
        );
    }
}
