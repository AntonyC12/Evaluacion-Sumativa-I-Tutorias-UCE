package com.tutorias.infrastructure.adaptadores;

import com.tutorias.domain.entities.Tutoria;
import com.tutorias.domain.repositorios.TutoriaRepository;
import com.tutorias.infrastructure.documentos.TutoriaDocument;
import com.tutorias.infrastructure.repositorios.TutoriaMongoRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TutoriaRepositoryAdapter implements TutoriaRepository {
    private final TutoriaMongoRepository repository;

    @Override
    public Tutoria save(Tutoria tutoria) {
        TutoriaDocument doc = TutoriaDocument.fromDomain(tutoria);
        TutoriaDocument saved = repository.save(doc);
        return saved.toDomain();
    }

    @Override
    public Optional<Tutoria> findById(String id) {
        return repository.findById(id).map(TutoriaDocument::toDomain);
    }

    @Override
    public List<Tutoria> findByEstudianteId(String estudianteId) {
        return repository.findByEstudianteId(estudianteId).stream()
                .map(TutoriaDocument::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Tutoria> findByDocenteId(String docenteId) {
        return repository.findByDocenteId(docenteId).stream()
                .map(TutoriaDocument::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Tutoria> findAll() {
        return repository.findAll().stream()
                .map(TutoriaDocument::toDomain)
                .collect(Collectors.toList());
    }
}
