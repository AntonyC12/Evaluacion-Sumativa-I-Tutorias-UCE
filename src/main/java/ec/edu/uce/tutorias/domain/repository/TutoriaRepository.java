package ec.edu.uce.tutorias.domain.repository;

import ec.edu.uce.tutorias.domain.model.Tutoria;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TutoriaRepository {
    void guardar(Tutoria tutoria);
    Optional<Tutoria> buscarPorId(String id);
    List<Tutoria> buscarPorEstudianteYDocenteYMateriaEnRango(String estudianteId, String docenteId, String materiaId, LocalDateTime inicio, LocalDateTime fin);
    List<Tutoria> buscarSolapadasParaEstudiante(String estudianteId, LocalDateTime inicio, LocalDateTime fin);
    List<Tutoria> buscarSolapadasParaDocente(String docenteId, LocalDateTime inicio, LocalDateTime fin);
    List<Tutoria> buscarTodas();
}
