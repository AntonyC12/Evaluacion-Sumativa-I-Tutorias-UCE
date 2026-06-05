package com.tutorias.infrastructure.documentos;

import com.tutorias.domain.entities.Tutoria;
import com.tutorias.domain.enums.EstadoTutoria;
import com.tutorias.domain.valueobjects.FechaHoraTutoria;
import com.tutorias.domain.valueobjects.MotivoTutoria;
import com.tutorias.domain.valueobjects.RangoHorario;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tutorias")
public class TutoriaDocument {
    @Id
    private String id;
    private String estudianteId;
    private String docenteId;
    private String materiaId;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String motivo;
    private EstadoTutoria estado;
    private String disponibilidadId;

    public static TutoriaDocument fromDomain(Tutoria t) {
        if (t == null) return null;
        return new TutoriaDocument(
            t.getId(),
            t.getEstudianteId(),
            t.getDocenteId(),
            t.getMateriaId(),
            t.getFechaHora() != null ? t.getFechaHora().fecha() : null,
            t.getFechaHora() != null && t.getFechaHora().rangoHorario() != null ? t.getFechaHora().rangoHorario().horaInicio() : null,
            t.getFechaHora() != null && t.getFechaHora().rangoHorario() != null ? t.getFechaHora().rangoHorario().horaFin() : null,
            t.getMotivo() != null ? t.getMotivo().valor() : null,
            t.getEstado(),
            t.getDisponibilidadId()
        );
    }

    public Tutoria toDomain() {
        return new Tutoria(
            this.id,
            this.estudianteId,
            this.docenteId,
            this.materiaId,
            new FechaHoraTutoria(this.fecha, new RangoHorario(this.horaInicio, this.horaFin)),
            this.motivo != null ? new MotivoTutoria(this.motivo) : null,
            this.estado,
            this.disponibilidadId
        );
    }
}
