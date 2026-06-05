package com.tutorias.infrastructure.repositorios;

import com.tutorias.infrastructure.documentos.HistorialTutoriaDocument;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HistorialTutoriaMongoRepository extends MongoRepository<HistorialTutoriaDocument, String> {
    List<HistorialTutoriaDocument> findByTutoriaId(String tutoriaId);
}
