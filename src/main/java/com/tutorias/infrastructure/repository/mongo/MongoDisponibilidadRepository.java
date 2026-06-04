package com.tutorias.infrastructure.repository.mongo;

import com.tutorias.infrastructure.repository.mongo.document.DisponibilidadDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MongoDisponibilidadRepository extends MongoRepository<DisponibilidadDocument, String> {
    List<DisponibilidadDocument> findByDocenteId(String docenteId);
    List<DisponibilidadDocument> findByEstado(String estado);
    List<DisponibilidadDocument> findByDocenteIdAndFechaAndEstado(String docenteId, String fecha, String estado);
}
