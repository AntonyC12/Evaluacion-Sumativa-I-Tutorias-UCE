package com.tutorias.infrastructure.adaptadores;

import com.tutorias.domain.entities.Usuario;
import com.tutorias.domain.repositorios.UsuarioRepository;
import com.tutorias.domain.valueobjects.CorreoInstitucional;
import com.tutorias.infrastructure.documentos.UsuarioDocument;
import com.tutorias.infrastructure.repositorios.UsuarioMongoRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UsuarioRepositoryAdapter implements UsuarioRepository {
    private final UsuarioMongoRepository repository;

    @Override
    public Usuario save(Usuario usuario) {
        UsuarioDocument doc = UsuarioDocument.fromDomain(usuario);
        UsuarioDocument saved = repository.save(doc);
        return saved.toDomain();
    }

    @Override
    public Optional<Usuario> findById(String id) {
        return repository.findById(id).map(UsuarioDocument::toDomain);
    }

    @Override
    public Optional<Usuario> findByCorreo(CorreoInstitucional correo) {
        if (correo == null) return Optional.empty();
        return repository.findByCorreo(correo.valor()).map(UsuarioDocument::toDomain);
    }

    @Override
    public List<Usuario> findAll() {
        return repository.findAll().stream()
                .map(UsuarioDocument::toDomain)
                .collect(Collectors.toList());
    }
}
