package com.tutorias.infrastructure.repositorios;

import com.tutorias.infrastructure.documentos.DocenteDocument;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocenteMongoRepository extends MongoRepository<DocenteDocument, String> {
    Optional<DocenteDocument> findByUsuarioId(String usuarioId);
}
