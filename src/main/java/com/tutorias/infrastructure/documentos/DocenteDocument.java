package com.tutorias.infrastructure.documentos;

import com.tutorias.domain.entities.Docente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "docentes")
public class DocenteDocument {
    @Id
    private String id;
    private String usuarioId;
    private String nombre;
    private String cubiculo;

    public static DocenteDocument fromDomain(Docente d) {
        if (d == null) return null;
        return new DocenteDocument(
            d.getId(),
            d.getUsuarioId(),
            d.getNombre(),
            d.getCubiculo()
        );
    }

    public Docente toDomain() {
        return new Docente(
            this.id,
            this.usuarioId,
            this.nombre,
            this.cubiculo
        );
    }
}
