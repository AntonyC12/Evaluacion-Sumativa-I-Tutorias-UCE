package com.tutorias.infrastructure.documentos;

import com.tutorias.domain.entities.Estudiante;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "estudiantes")
public class EstudianteDocument {
    @Id
    private String id;
    private String usuarioId;
    private String nombre;
    private String carrera;

    public static EstudianteDocument fromDomain(Estudiante e) {
        if (e == null) return null;
        return new EstudianteDocument(
            e.getId(),
            e.getUsuarioId(),
            e.getNombre(),
            e.getCarrera()
        );
    }

    public Estudiante toDomain() {
        return new Estudiante(
            this.id,
            this.usuarioId,
            this.nombre,
            this.carrera
        );
    }
}
