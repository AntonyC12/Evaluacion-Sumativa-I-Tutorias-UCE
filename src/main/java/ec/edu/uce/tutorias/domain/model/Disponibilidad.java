package ec.edu.uce.tutorias.domain.model;

import ec.edu.uce.tutorias.domain.exception.DomainException;
import ec.edu.uce.tutorias.domain.vo.RangoHorario;

import java.time.LocalDate;

public class Disponibilidad {

    private final String id;
    private final String docenteId;
    private final String materiaId;
    private final LocalDate fecha;
    private final RangoHorario rangoHorario;
    private boolean reservado;

    public Disponibilidad(String id, String docenteId, String materiaId, LocalDate fecha, RangoHorario rangoHorario) {
        if (id == null || id.trim().isEmpty()) {
            throw new DomainException("El ID de disponibilidad es obligatorio");
        }
        if (docenteId == null || docenteId.trim().isEmpty()) {
            throw new DomainException("El ID del docente es obligatorio");
        }
        if (materiaId == null || materiaId.trim().isEmpty()) {
            throw new DomainException("El ID de la materia es obligatorio");
        }
        if (fecha == null) {
            throw new DomainException("La fecha es obligatoria");
        }
        if (fecha.isBefore(LocalDate.now())) {
            throw new DomainException("No se puede registrar disponibilidad en el pasado");
        }
        if (rangoHorario == null) {
            throw new DomainException("El rango horario es obligatorio");
        }

        this.id = id;
        this.docenteId = docenteId;
        this.materiaId = materiaId;
        this.fecha = fecha;
        this.rangoHorario = rangoHorario;
        this.reservado = false;
    }

    // Constructor para rehidratar desde la base de datos sin lanzar excepciones de negocio (ej: fechas pasadas permitidas al leer)
    public Disponibilidad(String id, String docenteId, String materiaId, LocalDate fecha, RangoHorario rangoHorario, boolean reservado) {
        this.id = id;
        this.docenteId = docenteId;
        this.materiaId = materiaId;
        this.fecha = fecha;
        this.rangoHorario = rangoHorario;
        this.reservado = reservado;
    }

    public void reservar() {
        if (this.reservado) {
            throw new DomainException("Esta disponibilidad ya se encuentra reservada");
        }
        this.reservado = true;
    }

    public void liberar() {
        if (!this.reservado) {
            throw new DomainException("Esta disponibilidad no estaba reservada");
        }
        this.reservado = false;
    }

    public String getId() {
        return id;
    }

    public String getDocenteId() {
        return docenteId;
    }

    public String getMateriaId() {
        return materiaId;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public RangoHorario getRangoHorario() {
        return rangoHorario;
    }

    public boolean isReservado() {
        return reservado;
    }
}
