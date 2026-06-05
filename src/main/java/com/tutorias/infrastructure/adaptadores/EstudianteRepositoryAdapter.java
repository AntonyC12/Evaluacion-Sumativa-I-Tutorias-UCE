package com.tutorias.infrastructure.adaptadores;

import com.tutorias.domain.entities.Estudiante;
import com.tutorias.domain.repositorios.EstudianteRepository;
import com.tutorias.infrastructure.documentos.EstudianteDocument;
import com.tutorias.infrastructure.repositorios.EstudianteMongoRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EstudianteRepositoryAdapter implements EstudianteRepository {
    private final EstudianteMongoRepository repository;

    @Override
    public Estudiante save(Estudiante estudiante) {
        EstudianteDocument doc = EstudianteDocument.fromDomain(estudiante);
        EstudianteDocument saved = repository.save(doc);
        return saved.toDomain();
    }

    @Override
    public Optional<Estudiante> findById(String id) {
        return repository.findById(id).map(EstudianteDocument::toDomain);
    }

    @Override
    public Optional<Estudiante> findByUsuarioId(String usuarioId) {
        return repository.findByUsuarioId(usuarioId).map(EstudianteDocument::toDomain);
    }

    @Override
    public List<Estudiante> findAll() {
        return repository.findAll().stream()
                .map(EstudianteDocument::toDomain)
                .collect(Collectors.toList());
    }
}
