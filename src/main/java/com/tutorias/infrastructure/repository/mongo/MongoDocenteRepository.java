package com.tutorias.infrastructure.repository.mongo;

import com.tutorias.infrastructure.repository.mongo.document.DocenteDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface MongoDocenteRepository extends MongoRepository<DocenteDocument, String> {
    Optional<DocenteDocument> findByUsuarioId(String usuarioId);
}
