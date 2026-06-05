package com.tutorias.infrastructure.repositorios;

import com.tutorias.infrastructure.documentos.TutoriaDocument;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TutoriaMongoRepository extends MongoRepository<TutoriaDocument, String> {
    List<TutoriaDocument> findByEstudianteId(String estudianteId);
    List<TutoriaDocument> findByDocenteId(String docenteId);
}
