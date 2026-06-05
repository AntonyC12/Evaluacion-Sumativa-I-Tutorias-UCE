package com.tutorias.domain.repositorios;

import com.tutorias.domain.entities.Tutoria;
import java.util.List;
import java.util.Optional;

public interface TutoriaRepository {
    Tutoria save(Tutoria tutoria);
    Optional<Tutoria> findById(String id);
    List<Tutoria> findByEstudianteId(String estudianteId);
    List<Tutoria> findByDocenteId(String docenteId);
    List<Tutoria> findAll();
}
