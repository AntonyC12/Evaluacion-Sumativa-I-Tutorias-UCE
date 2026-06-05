package com.tutorias.domain.valueobjects;

import com.tutorias.domain.exceptions.DomainException;
import java.time.LocalTime;

public record RangoHorario(LocalTime horaInicio, LocalTime horaFin) {
    public RangoHorario {
        if (horaInicio == null || horaFin == null) {
            throw new DomainException("La hora de inicio y la hora de fin no pueden ser nulas.");
        }
        if (!horaInicio.isBefore(horaFin)) {
            throw new DomainException("La hora de inicio debe ser anterior a la hora de fin.");
        }
    }

    public boolean seSolapaCon(RangoHorario otro) {
        if (otro == null) return false;
        return this.horaInicio.isBefore(otro.horaFin()) && otro.horaInicio().isBefore(this.horaFin);
    }
}
