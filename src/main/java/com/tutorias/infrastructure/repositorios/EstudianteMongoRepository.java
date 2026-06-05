package com.tutorias.infrastructure.repositorios;

import com.tutorias.infrastructure.documentos.EstudianteDocument;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EstudianteMongoRepository extends MongoRepository<EstudianteDocument, String> {
    Optional<EstudianteDocument> findByUsuarioId(String usuarioId);
}
