package com.tutorias.infrastructure.repositorios;

import com.tutorias.infrastructure.documentos.UsuarioDocument;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsuarioMongoRepository extends MongoRepository<UsuarioDocument, String> {
    Optional<UsuarioDocument> findByCorreo(String correo);
}
