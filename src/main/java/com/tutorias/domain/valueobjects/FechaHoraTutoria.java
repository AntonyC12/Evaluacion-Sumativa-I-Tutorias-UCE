package com.tutorias.domain.valueobjects;

import com.tutorias.domain.exceptions.DomainException;
import java.time.LocalDate;

public record FechaHoraTutoria(LocalDate fecha, RangoHorario rangoHorario) {
    public FechaHoraTutoria {
        if (fecha == null || rangoHorario == null) {
            throw new DomainException("La fecha y el rango horario no pueden ser nulos.");
        }
    }

    public void validarFechaNoPasada(LocalDate fechaActual) {
        if (fecha.isBefore(fechaActual)) {
            throw new DomainException("No se puede agendar una tutoría en una fecha pasada.");
        }
    }
}
