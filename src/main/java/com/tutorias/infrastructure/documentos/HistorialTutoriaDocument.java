package com.tutorias.infrastructure.documentos;

import com.tutorias.domain.entities.HistorialTutoria;
import com.tutorias.domain.enums.EstadoTutoria;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "historial_tutorias")
public class HistorialTutoriaDocument {
    @Id
    private String id;
    private String tutoriaId;
    private EstadoTutoria estadoAnterior;
    private EstadoTutoria estadoNuevo;
    private LocalDateTime fechaCambio;
    private String modificadoPor;
    private String detalle;

    public static HistorialTutoriaDocument fromDomain(HistorialTutoria h) {
        if (h == null) return null;
        return new HistorialTutoriaDocument(
            h.getId(),
            h.getTutoriaId(),
            h.getEstadoAnterior(),
            h.getEstadoNuevo(),
            h.getFechaCambio(),
            h.getModificadoPor(),
            h.getDetalle()
        );
    }

    public HistorialTutoria toDomain() {
        return new HistorialTutoria(
            this.id,
            this.tutoriaId,
            this.estadoAnterior,
            this.estadoNuevo,
            this.fechaCambio,
            this.modificadoPor,
            this.detalle
        );
    }
}
