package ec.edu.uce.tutorias.domain.model;

import ec.edu.uce.tutorias.domain.exception.DomainException;
import ec.edu.uce.tutorias.domain.vo.EstadoTutoria;
import ec.edu.uce.tutorias.domain.vo.MotivoTutoria;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Tutoria {

    private final String id;
    private final String estudianteId;
    private final String docenteId;
    private final String materiaId;
    private final String disponibilidadId;
    private final LocalDateTime fechaHoraInicio;
    private final LocalDateTime fechaHoraFin;
    private final MotivoTutoria motivo;
    private EstadoTutoria estado;

    public Tutoria(String id, String estudianteId, String docenteId, String materiaId, String disponibilidadId, 
                   LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin, MotivoTutoria motivo) {
        
        if (id == null || id.trim().isEmpty()) throw new DomainException("ID de tutoría es obligatorio");
        if (estudianteId == null || estudianteId.trim().isEmpty()) throw new DomainException("ID de estudiante es obligatorio");
        if (docenteId == null || docenteId.trim().isEmpty()) throw new DomainException("ID de docente es obligatorio");
        if (materiaId == null || materiaId.trim().isEmpty()) throw new DomainException("ID de materia es obligatorio");
        if (disponibilidadId == null || disponibilidadId.trim().isEmpty()) throw new DomainException("ID de disponibilidad es obligatorio");
        if (fechaHoraInicio == null || fechaHoraFin == null) throw new DomainException("Las fechas y horas son obligatorias");
        if (motivo == null) throw new DomainException("El motivo es obligatorio");
        
        if (fechaHoraInicio.isBefore(LocalDateTime.now())) {
            throw new DomainException("No se pueden agendar tutorías en el pasado");
        }
        
        this.id = id;
        this.estudianteId = estudianteId;
        this.docenteId = docenteId;
        this.materiaId = materiaId;
        this.disponibilidadId = disponibilidadId;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.motivo = motivo;
        this.estado = EstadoTutoria.PENDIENTE;
    }

    // Constructor para rehidratar
    public Tutoria(String id, String estudianteId, String docenteId, String materiaId, String disponibilidadId, 
                   LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin, MotivoTutoria motivo, EstadoTutoria estado) {
        this.id = id;
        this.estudianteId = estudianteId;
        this.docenteId = docenteId;
        this.materiaId = materiaId;
        this.disponibilidadId = disponibilidadId;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.motivo = motivo;
        this.estado = estado;
    }

    public void cancelar() {
        if (this.estado == EstadoTutoria.CANCELADA) {
            throw new DomainException("La tutoría ya está cancelada");
        }
        if (this.estado == EstadoTutoria.ASISTIDA || this.estado == EstadoTutoria.INASISTENCIA) {
            throw new DomainException("No se puede cancelar una tutoría que ya ha ocurrido");
        }
        
        long horasFaltantes = ChronoUnit.HOURS.between(LocalDateTime.now(), this.fechaHoraInicio);
        if (horasFaltantes < 24) {
            throw new DomainException("Solo se puede cancelar con al menos 24 horas de anticipación");
        }
        
        this.estado = EstadoTutoria.CANCELADA;
    }

    public void confirmar() {
        if (this.estado == EstadoTutoria.CANCELADA) {
            throw new DomainException("No se puede confirmar una tutoría cancelada");
        }
        if (this.estado != EstadoTutoria.PENDIENTE) {
            throw new DomainException("Solo las tutorías pendientes pueden ser confirmadas");
        }
        this.estado = EstadoTutoria.CONFIRMADA;
    }

    public void registrarAsistencia() {
        if (this.estado == EstadoTutoria.CANCELADA) {
            throw new DomainException("No se puede registrar asistencia de una tutoría cancelada");
        }
        if (this.estado != EstadoTutoria.CONFIRMADA) {
            throw new DomainException("Solo se puede registrar asistencia de una tutoría confirmada");
        }
        this.estado = EstadoTutoria.ASISTIDA;
    }

    public void registrarInasistencia() {
        if (this.estado == EstadoTutoria.CANCELADA) {
            throw new DomainException("No se puede registrar inasistencia de una tutoría cancelada");
        }
        if (this.estado != EstadoTutoria.CONFIRMADA) {
            throw new DomainException("Solo se puede registrar inasistencia de una tutoría confirmada");
        }
        this.estado = EstadoTutoria.INASISTENCIA;
    }

    // Getters
    public String getId() { return id; }
    public String getEstudianteId() { return estudianteId; }
    public String getDocenteId() { return docenteId; }
    public String getMateriaId() { return materiaId; }
    public String getDisponibilidadId() { return disponibilidadId; }
    public LocalDateTime getFechaHoraInicio() { return fechaHoraInicio; }
    public LocalDateTime getFechaHoraFin() { return fechaHoraFin; }
    public MotivoTutoria getMotivo() { return motivo; }
    public EstadoTutoria getEstado() { return estado; }
}
