package com.tutorias.domain.repositorios;

import com.tutorias.domain.entities.Materia;
import java.util.List;
import java.util.Optional;

public interface MateriaRepository {
    Materia save(Materia materia);
    Optional<Materia> findById(String id);
    Optional<Materia> findByCodigo(String codigo);
    List<Materia> findAll();
}
