package com.tutorias.infrastructure.repository.mongo;

import com.tutorias.infrastructure.repository.mongo.document.UsuarioDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface MongoUsuarioRepository extends MongoRepository<UsuarioDocument, String> {
    Optional<UsuarioDocument> findByCorreoInstitucional(String correo);
    List<UsuarioDocument> findByRol(String rol);
}
