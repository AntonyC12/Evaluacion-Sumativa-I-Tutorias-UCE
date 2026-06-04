package com.tutorias.domain.model;

import com.tutorias.domain.exception.DomainException;
import java.time.LocalDate;
import java.time.LocalTime;

public class Disponibilidad {
    private String id;
    private String docenteId;
    private String docenteNombre;
    private String materiaId;
    private String materiaNombre;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String estado; // Disponible, Reservado

    public Disponibilidad() {}

    public Disponibilidad(String id, String docenteId, String docenteNombre, String materiaId, String materiaNombre,
                          LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, String estado) {
        this.id = id;
        this.docenteId = docenteId;
        this.docenteNombre = docenteNombre;
        this.materiaId = materiaId;
        this.materiaNombre = materiaNombre;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = estado;
        validate();
    }

    public void validate() {
        if (docenteId == null || docenteId.trim().isEmpty()) {
            throw new DomainException("El ID del docente es obligatorio");
        }
        if (materiaId == null || materiaId.trim().isEmpty()) {
            throw new DomainException("El ID de la materia es obligatorio");
        }
        if (fecha == null) {
            throw new DomainException("La fecha es obligatoria");
        }
        if (horaInicio == null || horaFin == null) {
            throw new DomainException("Las horas de inicio y fin son obligatorias");
        }
        if (!horaInicio.isBefore(horaFin)) {
            throw new DomainException("La hora de inicio debe ser anterior a la hora de fin");
        }
        if (estado == null || (!estado.equals("Disponible") && !estado.equals("Reservado"))) {
            throw new DomainException("El estado de disponibilidad debe ser Disponible o Reservado");
        }
    }

    public void reservar() {
        if (!"Disponible".equals(this.estado)) {
            throw new DomainException("El horario seleccionado no existe o ya no está disponible");
        }
        this.estado = "Reservado";
    }

    public void liberar() {
        this.estado = "Disponible";
    }

    public void verificarDisponibilidadEdicion() {
        if ("Reservado".equals(this.estado)) {
            throw new DomainException("Un horario ya reservado no puede ser modificado o eliminado");
        }
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDocenteId() { return docenteId; }
    public void setDocenteId(String docenteId) { this.docenteId = docenteId; }

    public String getDocenteNombre() { return docenteNombre; }
    public void setDocenteNombre(String docenteNombre) { this.docenteNombre = docenteNombre; }

    public String getMateriaId() { return materiaId; }
    public void setMateriaId(String materiaId) { this.materiaId = materiaId; }

    public String getMateriaNombre() { return materiaNombre; }
    public void setMateriaNombre(String materiaNombre) { this.materiaNombre = materiaNombre; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
