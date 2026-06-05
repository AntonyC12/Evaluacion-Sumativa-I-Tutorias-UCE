package com.tutorias.infrastructure.adaptadores;

import com.tutorias.domain.entities.Docente;
import com.tutorias.domain.repositorios.DocenteRepository;
import com.tutorias.infrastructure.documentos.DocenteDocument;
import com.tutorias.infrastructure.repositorios.DocenteMongoRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DocenteRepositoryAdapter implements DocenteRepository {
    private final DocenteMongoRepository repository;

    @Override
    public Docente save(Docente docente) {
        DocenteDocument doc = DocenteDocument.fromDomain(docente);
        DocenteDocument saved = repository.save(doc);
        return saved.toDomain();
    }

    @Override
    public Optional<Docente> findById(String id) {
        return repository.findById(id).map(DocenteDocument::toDomain);
    }

    @Override
    public Optional<Docente> findByUsuarioId(String usuarioId) {
        return repository.findByUsuarioId(usuarioId).map(DocenteDocument::toDomain);
    }

    @Override
    public List<Docente> findAll() {
        return repository.findAll().stream()
                .map(DocenteDocument::toDomain)
                .collect(Collectors.toList());
    }
}
