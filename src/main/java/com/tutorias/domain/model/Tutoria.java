package com.tutorias.domain.model;

import com.tutorias.domain.enums.EstadoTutoria;
import com.tutorias.domain.valueobject.MotivoTutoria;
import com.tutorias.domain.exception.DomainException;
import java.time.LocalDateTime;
import java.time.Duration;

public class Tutoria {
    private String id;
    private String estudianteId;
    private String estudianteNombre;
    private String docenteId;
    private String docenteNombre;
    private String materiaId;
    private String materiaNombre;
    private String disponibilidadId;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private MotivoTutoria motivo;
    private EstadoTutoria estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private String observacion;

    public Tutoria() {}

    public Tutoria(String id, String estudianteId, String estudianteNombre, String docenteId, String docenteNombre,
                   String materiaId, String materiaNombre, String disponibilidadId, LocalDateTime fechaHoraInicio,
                   LocalDateTime fechaHoraFin, MotivoTutoria motivo, EstadoTutoria estado, LocalDateTime fechaCreacion,
                   LocalDateTime fechaActualizacion) {
        this.id = id;
        this.estudianteId = estudianteId;
        this.estudianteNombre = estudianteNombre;
        this.docenteId = docenteId;
        this.docenteNombre = docenteNombre;
        this.materiaId = materiaId;
        this.materiaNombre = materiaNombre;
        this.disponibilidadId = disponibilidadId;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.motivo = motivo;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
        validate();
    }

    public void validate() {
        if (estudianteId == null || estudianteId.trim().isEmpty()) {
            throw new DomainException("El estudiante es obligatorio");
        }
        if (docenteId == null || docenteId.trim().isEmpty()) {
            throw new DomainException("El docente es obligatorio");
        }
        if (materiaId == null || materiaId.trim().isEmpty()) {
            throw new DomainException("La materia es obligatoria");
        }
        if (disponibilidadId == null || disponibilidadId.trim().isEmpty()) {
            throw new DomainException("La disponibilidad asociada es obligatoria");
        }
        if (fechaHoraInicio == null || fechaHoraFin == null) {
            throw new DomainException("Las fechas y horas de inicio y fin son obligatorias");
        }
        if (!fechaHoraInicio.isBefore(fechaHoraFin)) {
            throw new DomainException("La fecha y hora de inicio debe ser anterior a la de fin");
        }
        if (motivo == null) {
            throw new DomainException("El motivo de la tutoría es obligatorio");
        }
        if (estado == null) {
            throw new DomainException("El estado de la tutoría es obligatorio");
        }
    }

    public void confirmar(LocalDateTime ahora) {
        if (this.estado != EstadoTutoria.PENDIENTE) {
            throw new DomainException("Solo se puede confirmar una tutoría en estado Pendiente");
        }
        this.estado = EstadoTutoria.CONFIRMADA;
        this.fechaActualizacion = ahora;
    }

    public void registrarAsistencia(String observacion, LocalDateTime ahora) {
        if (this.estado != EstadoTutoria.CONFIRMADA) {
            throw new DomainException("Solo se puede registrar asistencia en tutorías Confirmadas");
        }
        if (observacion == null || observacion.trim().isEmpty()) {
            throw new DomainException("La observación es obligatoria al registrar la asistencia");
        }
        this.estado = EstadoTutoria.ASISTIDA;
        this.observacion = observacion;
        this.fechaActualizacion = ahora;
    }

    public void registrarInasistencia(String observacion, LocalDateTime ahora) {
        if (this.estado != EstadoTutoria.CONFIRMADA) {
            throw new DomainException("Solo se puede registrar inasistencia en tutorías Confirmadas");
        }
        if (observacion == null || observacion.trim().isEmpty()) {
            throw new DomainException("La observación es obligatoria al registrar la inasistencia");
        }
        this.estado = EstadoTutoria.INASISTENCIA;
        this.observacion = observacion;
        this.fechaActualizacion = ahora;
    }

    public void cancelar(String rolUsuario, LocalDateTime ahora) {
        // Validar transiciones desde estados terminales
        if (this.estado == EstadoTutoria.ASISTIDA || this.estado == EstadoTutoria.INASISTENCIA || this.estado == EstadoTutoria.CANCELADA) {
            throw new DomainException("No se permiten transiciones desde estados terminales (Asistida, Inasistencia, Cancelada)");
        }

        // Si es estudiante, aplicar la regla de las 24 horas
        if ("estudiante".equals(rolUsuario)) {
            Duration duracion = Duration.between(ahora, this.fechaHoraInicio);
            if (duracion.toHours() < 24) {
                throw new DomainException("No puedes cancelar esta tutoría. Falta menos de 24 horas para su inicio.");
            }
        }

        // Si pasa la validación, cancelar
        this.estado = EstadoTutoria.CANCELADA;
        this.fechaActualizacion = ahora;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEstudianteId() { return estudianteId; }
    public void setEstudianteId(String estudianteId) { this.estudianteId = estudianteId; }

    public String getEstudianteNombre() { return estudianteNombre; }
    public void setEstudianteNombre(String estudianteNombre) { this.estudianteNombre = estudianteNombre; }

    public String getDocenteId() { return docenteId; }
    public void setDocenteId(String docenteId) { this.docenteId = docenteId; }

    public String getDocenteNombre() { return docenteNombre; }
    public void setDocenteNombre(String docenteNombre) { this.docenteNombre = docenteNombre; }

    public String getMateriaId() { return materiaId; }
    public void setMateriaId(String materiaId) { this.materiaId = materiaId; }

    public String getMateriaNombre() { return materiaNombre; }
    public void setMateriaNombre(String materiaNombre) { this.materiaNombre = materiaNombre; }

    public String getDisponibilidadId() { return disponibilidadId; }
    public void setDisponibilidadId(String disponibilidadId) { this.disponibilidadId = disponibilidadId; }

    public LocalDateTime getFechaHoraInicio() { return fechaHoraInicio; }
    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) { this.fechaHoraInicio = fechaHoraInicio; }

    public LocalDateTime getFechaHoraFin() { return fechaHoraFin; }
    public void setFechaHoraFin(LocalDateTime fechaHoraFin) { this.fechaHoraFin = fechaHoraFin; }

    public MotivoTutoria getMotivo() { return motivo; }
    public void setMotivo(MotivoTutoria motivo) { this.motivo = motivo; }

    public EstadoTutoria getEstado() { return estado; }
    public void setEstado(EstadoTutoria estado) { this.estado = estado; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}
