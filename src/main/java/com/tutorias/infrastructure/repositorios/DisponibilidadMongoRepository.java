package com.tutorias.infrastructure.repositorios;

import com.tutorias.infrastructure.documentos.DisponibilidadDocument;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DisponibilidadMongoRepository extends MongoRepository<DisponibilidadDocument, String> {
    List<DisponibilidadDocument> findByDocenteId(String docenteId);
}
