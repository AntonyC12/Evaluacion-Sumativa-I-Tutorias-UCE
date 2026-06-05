package ec.edu.uce.tutorias.application.usecase;

import ec.edu.uce.tutorias.domain.model.Materia;
import ec.edu.uce.tutorias.domain.repository.MateriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarMateriasUseCase {

    private final MateriaRepository materiaRepository;

    public ListarMateriasUseCase(MateriaRepository materiaRepository) {
        this.materiaRepository = materiaRepository;
    }

    public List<Materia> ejecutar() {
        return materiaRepository.buscarTodas();
    }
}
