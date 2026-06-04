package com.tutorias.infrastructure.repository.mongo;

import com.tutorias.infrastructure.repository.mongo.document.TutoriaDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MongoTutoriaRepository extends MongoRepository<TutoriaDocument, String> {
    List<TutoriaDocument> findByEstudianteId(String estudianteId);
    List<TutoriaDocument> findByDocenteId(String docenteId);
    List<TutoriaDocument> findByEstudianteIdAndDocenteIdAndMateriaIdAndEstadoNot(String estudianteId, String docenteId, String materiaId, String estadoExcluido);
    List<TutoriaDocument> findByEstudianteIdAndEstadoIn(String estudianteId, List<String> estados);
}
