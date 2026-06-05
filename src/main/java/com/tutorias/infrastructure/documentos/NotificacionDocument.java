package com.tutorias.infrastructure.documentos;

import com.tutorias.domain.entities.Notificacion;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notificaciones")
public class NotificacionDocument {
    @Id
    private String id;
    private String usuarioId;
    private String mensaje;
    private LocalDateTime fechaCreacion;
    private boolean leido;

    public static NotificacionDocument fromDomain(Notificacion n) {
        if (n == null) return null;
        return new NotificacionDocument(
            n.getId(),
            n.getUsuarioId(),
            n.getMensaje(),
            n.getFechaCreacion(),
            n.isLeido()
        );
    }

    public Notificacion toDomain() {
        return new Notificacion(
            this.id,
            this.usuarioId,
            this.mensaje,
            this.fechaCreacion,
            this.leido
        );
    }
}
