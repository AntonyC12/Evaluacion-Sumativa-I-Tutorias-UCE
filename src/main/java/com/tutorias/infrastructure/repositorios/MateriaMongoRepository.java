package com.tutorias.infrastructure.repositorios;

import com.tutorias.infrastructure.documentos.MateriaDocument;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MateriaMongoRepository extends MongoRepository<MateriaDocument, String> {
    Optional<MateriaDocument> findByCodigo(String codigo);
}
