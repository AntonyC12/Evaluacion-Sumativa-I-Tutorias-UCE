package ec.edu.uce.tutorias.infrastructure.persistence.repository;

import ec.edu.uce.tutorias.infrastructure.persistence.document.DisponibilidadDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SpringDataDisponibilidadMongoRepository extends MongoRepository<DisponibilidadDocument, String> {
    List<DisponibilidadDocument> findByDocenteIdAndFecha(String docenteId, LocalDate fecha);
}
