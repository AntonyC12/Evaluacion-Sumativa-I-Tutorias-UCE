package ec.edu.uce.tutorias.application.usecase;

import ec.edu.uce.tutorias.domain.model.Tutoria;
import ec.edu.uce.tutorias.domain.repository.TutoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarTutoriasUseCase {

    private final TutoriaRepository tutoriaRepository;

    public ListarTutoriasUseCase(TutoriaRepository tutoriaRepository) {
        this.tutoriaRepository = tutoriaRepository;
    }

    public List<Tutoria> ejecutar() {
        return tutoriaRepository.buscarTodas();
    }
}
