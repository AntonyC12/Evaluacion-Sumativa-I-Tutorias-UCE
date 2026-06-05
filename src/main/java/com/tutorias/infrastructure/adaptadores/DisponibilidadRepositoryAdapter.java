package com.tutorias.infrastructure.adaptadores;

import com.tutorias.domain.entities.Disponibilidad;
import com.tutorias.domain.repositorios.DisponibilidadRepository;
import com.tutorias.infrastructure.documentos.DisponibilidadDocument;
import com.tutorias.infrastructure.repositorios.DisponibilidadMongoRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DisponibilidadRepositoryAdapter implements DisponibilidadRepository {
    private final DisponibilidadMongoRepository repository;

    @Override
    public Disponibilidad save(Disponibilidad disponibilidad) {
        DisponibilidadDocument doc = DisponibilidadDocument.fromDomain(disponibilidad);
        DisponibilidadDocument saved = repository.save(doc);
        return saved.toDomain();
    }

    @Override
    public Optional<Disponibilidad> findById(String id) {
        return repository.findById(id).map(DisponibilidadDocument::toDomain);
    }

    @Override
    public List<Disponibilidad> findByDocenteId(String docenteId) {
        return repository.findByDocenteId(docenteId).stream()
                .map(DisponibilidadDocument::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Disponibilidad> findAll() {
        return repository.findAll().stream()
                .map(DisponibilidadDocument::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }
}
