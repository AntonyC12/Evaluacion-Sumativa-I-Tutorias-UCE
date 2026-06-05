package com.tutorias.infrastructure.documentos;

import com.tutorias.domain.entities.Disponibilidad;
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
@Document(collection = "disponibilidades")
public class DisponibilidadDocument {
    @Id
    private String id;
    private String docenteId;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private boolean reservada;

    public static DisponibilidadDocument fromDomain(Disponibilidad d) {
        if (d == null) return null;
        return new DisponibilidadDocument(
            d.getId(),
            d.getDocenteId(),
            d.getFecha(),
            d.getRangoHorario().horaInicio(),
            d.getRangoHorario().horaFin(),
            d.isReservada()
        );
    }

    public Disponibilidad toDomain() {
        return new Disponibilidad(
            this.id,
            this.docenteId,
            this.fecha,
            new RangoHorario(this.horaInicio, this.horaFin),
            this.reservada
        );
    }
}
