package com.tutorias.infrastructure.repositorios;

import com.tutorias.infrastructure.documentos.NotificacionDocument;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificacionMongoRepository extends MongoRepository<NotificacionDocument, String> {
    List<NotificacionDocument> findByUsuarioId(String usuarioId);
}
