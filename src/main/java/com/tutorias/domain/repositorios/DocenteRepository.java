package com.tutorias.domain.repositorios;

import com.tutorias.domain.entities.Docente;
import java.util.List;
import java.util.Optional;

public interface DocenteRepository {
    Docente save(Docente docente);
    Optional<Docente> findById(String id);
    Optional<Docente> findByUsuarioId(String usuarioId);
    List<Docente> findAll();
}
