package com.tutorias.domain.repositorios;

import com.tutorias.domain.entities.Disponibilidad;
import java.util.List;
import java.util.Optional;

public interface DisponibilidadRepository {
    Disponibilidad save(Disponibilidad disponibilidad);
    Optional<Disponibilidad> findById(String id);
    List<Disponibilidad> findByDocenteId(String docenteId);
    List<Disponibilidad> findAll();
    void delete(String id);
}
