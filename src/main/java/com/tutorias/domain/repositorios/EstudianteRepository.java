package com.tutorias.domain.repositorios;

import com.tutorias.domain.entities.Estudiante;
import java.util.List;
import java.util.Optional;

public interface EstudianteRepository {
    Estudiante save(Estudiante estudiante);
    Optional<Estudiante> findById(String id);
    Optional<Estudiante> findByUsuarioId(String usuarioId);
    List<Estudiante> findAll();
}
