package ec.edu.uce.tutorias.application.usecase;

import ec.edu.uce.tutorias.domain.exception.DomainException;
import ec.edu.uce.tutorias.domain.model.Tutoria;
import ec.edu.uce.tutorias.domain.repository.TutoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrarInasistenciaUseCase {

    private final TutoriaRepository tutoriaRepository;

    public RegistrarInasistenciaUseCase(TutoriaRepository tutoriaRepository) {
        this.tutoriaRepository = tutoriaRepository;
    }

    @Transactional
    public void ejecutar(String tutoriaId, String docenteId) {
        Tutoria tutoria = tutoriaRepository.buscarPorId(tutoriaId)
                .orElseThrow(() -> new DomainException("Tutoría no encontrada"));

        if (!tutoria.getDocenteId().equals(docenteId)) {
            throw new DomainException("Solo el docente asignado puede registrar la inasistencia");
        }

        tutoria.registrarInasistencia();
        tutoriaRepository.guardar(tutoria);
    }
}
