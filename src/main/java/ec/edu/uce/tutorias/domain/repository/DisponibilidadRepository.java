package ec.edu.uce.tutorias.domain.repository;

import ec.edu.uce.tutorias.domain.model.Disponibilidad;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DisponibilidadRepository {
    void guardar(Disponibilidad disponibilidad);
    Optional<Disponibilidad> buscarPorId(String id);
    List<Disponibilidad> buscarPorDocenteYFecha(String docenteId, LocalDate fecha);
    List<Disponibilidad> buscarTodas();
}
