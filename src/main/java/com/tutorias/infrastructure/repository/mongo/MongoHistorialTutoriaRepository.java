package com.tutorias.infrastructure.repository.mongo;

import com.tutorias.infrastructure.repository.mongo.document.HistorialTutoriaDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MongoHistorialTutoriaRepository extends MongoRepository<HistorialTutoriaDocument, String> {
    List<HistorialTutoriaDocument> findByTutoriaId(String tutoriaId);
}
