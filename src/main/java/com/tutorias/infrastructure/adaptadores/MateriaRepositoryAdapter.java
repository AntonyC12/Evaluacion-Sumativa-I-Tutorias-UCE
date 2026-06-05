package com.tutorias.infrastructure.adaptadores;

import com.tutorias.domain.entities.Materia;
import com.tutorias.domain.repositorios.MateriaRepository;
import com.tutorias.infrastructure.documentos.MateriaDocument;
import com.tutorias.infrastructure.repositorios.MateriaMongoRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MateriaRepositoryAdapter implements MateriaRepository {
    private final MateriaMongoRepository repository;

    @Override
    public Materia save(Materia materia) {
        MateriaDocument doc = MateriaDocument.fromDomain(materia);
        MateriaDocument saved = repository.save(doc);
        return saved.toDomain();
    }

    @Override
    public Optional<Materia> findById(String id) {
        return repository.findById(id).map(MateriaDocument::toDomain);
    }

    @Override
    public Optional<Materia> findByCodigo(String codigo) {
        return repository.findByCodigo(codigo).map(MateriaDocument::toDomain);
    }

    @Override
    public List<Materia> findAll() {
        return repository.findAll().stream()
                .map(MateriaDocument::toDomain)
                .collect(Collectors.toList());
    }
}
