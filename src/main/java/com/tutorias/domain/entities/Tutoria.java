package com.tutorias.domain.entities;

import com.tutorias.domain.enums.EstadoTutoria;
import com.tutorias.domain.exceptions.DomainException;
import com.tutorias.domain.valueobjects.FechaHoraTutoria;
import com.tutorias.domain.valueobjects.MotivoTutoria;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tutoria {
    private String id;
    private String estudianteId;
    private String docenteId;
    private String materiaId;
    private FechaHoraTutoria fechaHora;
    private MotivoTutoria motivo;
    private EstadoTutoria estado;
    private String disponibilidadId;

    public void confirmar() {
        if (this.estado != EstadoTutoria.PENDIENTE) {
            throw new DomainException("Solo se puede confirmar una tutoría en estado PENDIENTE. Estado actual: " + this.estado);
        }
        this.estado = EstadoTutoria.CONFIRMADA;
    }

    public void marcarAsistencia() {
        if (this.estado != EstadoTutoria.CONFIRMADA) {
            throw new DomainException("Solo se puede registrar asistencia para tutorías en estado CONFIRMADA. Estado actual: " + this.estado);
        }
        this.estado = EstadoTutoria.ASISTIDA;
    }

    public void marcarInasistencia() {
        if (this.estado != EstadoTutoria.CONFIRMADA) {
            throw new DomainException("Solo se puede registrar inasistencia para tutorías en estado CONFIRMADA. Estado actual: " + this.estado);
        }
        this.estado = EstadoTutoria.INASISTENCIA;
    }

    public void cancelar(LocalDateTime fechaHoraActual) {
        if (this.estado == EstadoTutoria.CANCELADA || this.estado == EstadoTutoria.ASISTIDA || this.estado == EstadoTutoria.INASISTENCIA) {
            throw new DomainException("No se puede cancelar una tutoría en estado: " + this.estado);
        }

        // Rule: Cancel with at least 24 hours prior notice
        LocalDateTime inicioTutoria = LocalDateTime.of(this.fechaHora.fecha(), this.fechaHora.rangoHorario().horaInicio());
        if (fechaHoraActual.plusHours(24).isAfter(inicioTutoria)) {
            throw new DomainException("La cancelación debe ser con al menos 24 horas de anticipación.");
        }

        this.estado = EstadoTutoria.CANCELADA;
    }
}
