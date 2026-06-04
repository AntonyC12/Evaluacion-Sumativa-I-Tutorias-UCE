package com.tutorias.infrastructure.repository.mongo;

import com.tutorias.infrastructure.repository.mongo.document.NotificacionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MongoNotificacionRepository extends MongoRepository<NotificacionDocument, String> {
    List<NotificacionDocument> findByDestinatario(String destinatario);
}
