package com.tutorias.infrastructure.adaptadores;

import com.tutorias.domain.entities.HistorialTutoria;
import com.tutorias.domain.repositorios.HistorialTutoriaRepository;
import com.tutorias.infrastructure.documentos.HistorialTutoriaDocument;
import com.tutorias.infrastructure.repositorios.HistorialTutoriaMongoRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HistorialTutoriaRepositoryAdapter implements HistorialTutoriaRepository {
    private final HistorialTutoriaMongoRepository repository;

    @Override
    public HistorialTutoria save(HistorialTutoria historial) {
        HistorialTutoriaDocument doc = HistorialTutoriaDocument.fromDomain(historial);
        HistorialTutoriaDocument saved = repository.save(doc);
        return saved.toDomain();
    }

    @Override
    public List<HistorialTutoria> findByTutoriaId(String tutoriaId) {
        return repository.findByTutoriaId(tutoriaId).stream()
                .map(HistorialTutoriaDocument::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<HistorialTutoria> findAll() {
        return repository.findAll().stream()
                .map(HistorialTutoriaDocument::toDomain)
                .collect(Collectors.toList());
    }
}
