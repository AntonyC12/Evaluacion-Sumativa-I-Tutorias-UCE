package ec.edu.uce.tutorias.infrastructure.persistence.repository;

import ec.edu.uce.tutorias.infrastructure.persistence.document.TutoriaDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SpringDataTutoriaMongoRepository extends MongoRepository<TutoriaDocument, String> {
    
    @Query("{ 'estudianteId': ?0, 'docenteId': ?1, 'materiaId': ?2, 'fechaHoraInicio': { $gte: ?3 }, 'fechaHoraFin': { $lte: ?4 } }")
    List<TutoriaDocument> findTutoriasMismaSemana(String estudianteId, String docenteId, String materiaId, LocalDateTime inicio, LocalDateTime fin);

    @Query("{ 'estudianteId': ?0, 'fechaHoraInicio': { $lt: ?2 }, 'fechaHoraFin': { $gt: ?1 } }")
    List<TutoriaDocument> findSolapadasEstudiante(String estudianteId, LocalDateTime inicio, LocalDateTime fin);

    @Query("{ 'docenteId': ?0, 'fechaHoraInicio': { $lt: ?2 }, 'fechaHoraFin': { $gt: ?1 } }")
    List<TutoriaDocument> findSolapadasDocente(String docenteId, LocalDateTime inicio, LocalDateTime fin);
}
