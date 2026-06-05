package ec.edu.uce.tutorias.application.usecase;

import ec.edu.uce.tutorias.domain.exception.DomainException;
import ec.edu.uce.tutorias.domain.model.Disponibilidad;
import ec.edu.uce.tutorias.domain.model.Tutoria;
import ec.edu.uce.tutorias.domain.repository.DisponibilidadRepository;
import ec.edu.uce.tutorias.domain.repository.TutoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CancelarTutoriaUseCase {

    private final TutoriaRepository tutoriaRepository;
    private final DisponibilidadRepository disponibilidadRepository;

    public CancelarTutoriaUseCase(TutoriaRepository tutoriaRepository, DisponibilidadRepository disponibilidadRepository) {
        this.tutoriaRepository = tutoriaRepository;
        this.disponibilidadRepository = disponibilidadRepository;
    }

    @Transactional
    public void ejecutar(String tutoriaId, String estudianteId) {
        Tutoria tutoria = tutoriaRepository.buscarPorId(tutoriaId)
                .orElseThrow(() -> new DomainException("Tutoría no encontrada"));

        if (!tutoria.getEstudianteId().equals(estudianteId)) {
            throw new DomainException("Solo el estudiante que solicitó la tutoría puede cancelarla");
        }

        // Regla de negocio de 24 horas protegida dentro de la entidad Tutoria
        tutoria.cancelar();
        tutoriaRepository.guardar(tutoria);

        // Liberar la disponibilidad
        Disponibilidad disponibilidad = disponibilidadRepository.buscarPorId(tutoria.getDisponibilidadId())
                .orElseThrow(() -> new DomainException("Disponibilidad asociada no encontrada"));
        
        disponibilidad.liberar();
        disponibilidadRepository.guardar(disponibilidad);
    }
}
