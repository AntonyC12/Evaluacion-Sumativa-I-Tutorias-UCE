package ec.edu.uce.tutorias.domain.vo;

import ec.edu.uce.tutorias.domain.exception.DomainException;
import java.time.LocalTime;
import java.util.Objects;

public class RangoHorario {
    
    private final LocalTime horaInicio;
    private final LocalTime horaFin;

    public RangoHorario(LocalTime horaInicio, LocalTime horaFin) {
        if (horaInicio == null || horaFin == null) {
            throw new DomainException("Las horas de inicio y fin son obligatorias");
        }
        if (!horaInicio.isBefore(horaFin)) {
            throw new DomainException("La hora de inicio debe ser anterior a la hora de fin");
        }
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public boolean seSolapaCon(RangoHorario otro) {
        return this.horaInicio.isBefore(otro.horaFin) && otro.horaInicio.isBefore(this.horaFin);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RangoHorario that = (RangoHorario) o;
        return Objects.equals(horaInicio, that.horaInicio) && Objects.equals(horaFin, that.horaFin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(horaInicio, horaFin);
    }
}
