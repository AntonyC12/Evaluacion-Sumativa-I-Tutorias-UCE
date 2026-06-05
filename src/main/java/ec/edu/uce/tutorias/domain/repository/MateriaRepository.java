package ec.edu.uce.tutorias.domain.repository;

import ec.edu.uce.tutorias.domain.model.Materia;
import java.util.List;
import java.util.Optional;

public interface MateriaRepository {
    void guardar(Materia materia);
    Optional<Materia> buscarPorId(String id);
    List<Materia> buscarTodas();
}
