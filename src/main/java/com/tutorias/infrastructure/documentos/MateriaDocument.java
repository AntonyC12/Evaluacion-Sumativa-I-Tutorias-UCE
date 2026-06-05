package com.tutorias.infrastructure.documentos;

import com.tutorias.domain.entities.Materia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "materias")
public class MateriaDocument {
    @Id
    private String id;
    private String nombre;
    private String codigo;

    public static MateriaDocument fromDomain(Materia m) {
        if (m == null) return null;
        return new MateriaDocument(
            m.getId(),
            m.getNombre(),
            m.getCodigo()
        );
    }

    public Materia toDomain() {
        return new Materia(
            this.id,
            this.nombre,
            this.codigo
        );
    }
}
