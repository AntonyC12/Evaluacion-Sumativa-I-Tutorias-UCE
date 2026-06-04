package com.tutorias.infrastructure.repository.mongo;

import com.tutorias.infrastructure.repository.mongo.document.EstudianteDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface MongoEstudianteRepository extends MongoRepository<EstudianteDocument, String> {
    Optional<EstudianteDocument> findByUsuarioId(String usuarioId);
}
