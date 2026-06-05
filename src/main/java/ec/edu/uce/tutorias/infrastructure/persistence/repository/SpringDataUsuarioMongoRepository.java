package ec.edu.uce.tutorias.infrastructure.persistence.repository;

import ec.edu.uce.tutorias.infrastructure.persistence.document.UsuarioDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataUsuarioMongoRepository extends MongoRepository<UsuarioDocument, String> {
    Optional<UsuarioDocument> findByCorreo(String correo);
}
