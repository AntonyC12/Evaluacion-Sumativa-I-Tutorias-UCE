package com.tutorias.domain.repositorios;

import com.tutorias.domain.entities.HistorialTutoria;
import java.util.List;

public interface HistorialTutoriaRepository {
    HistorialTutoria save(HistorialTutoria historial);
    List<HistorialTutoria> findByTutoriaId(String tutoriaId);
    List<HistorialTutoria> findAll();
}
