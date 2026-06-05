package com.tutorias.infrastructure.adaptadores;

import com.tutorias.domain.entities.Notificacion;
import com.tutorias.domain.repositorios.NotificacionRepository;
import com.tutorias.infrastructure.documentos.NotificacionDocument;
import com.tutorias.infrastructure.repositorios.NotificacionMongoRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificacionRepositoryAdapter implements NotificacionRepository {
    private final NotificacionMongoRepository repository;

    @Override
    public Notificacion save(Notificacion notificacion) {
        NotificacionDocument doc = NotificacionDocument.fromDomain(notificacion);
        NotificacionDocument saved = repository.save(doc);
        return saved.toDomain();
    }

    @Override
    public List<Notificacion> findByUsuarioId(String usuarioId) {
        return repository.findByUsuarioId(usuarioId).stream()
                .map(NotificacionDocument::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Notificacion> findAll() {
        return repository.findAll().stream()
                .map(NotificacionDocument::toDomain)
                .collect(Collectors.toList());
    }
}
