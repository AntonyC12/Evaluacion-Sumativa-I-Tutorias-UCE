package ec.edu.uce.tutorias.infrastructure.persistence.repository;

import ec.edu.uce.tutorias.infrastructure.persistence.document.MateriaDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataMateriaMongoRepository extends MongoRepository<MateriaDocument, String> {
}
