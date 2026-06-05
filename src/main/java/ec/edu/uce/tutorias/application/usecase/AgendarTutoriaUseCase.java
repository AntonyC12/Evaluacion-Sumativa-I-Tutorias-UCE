package ec.edu.uce.tutorias.application.usecase;

import ec.edu.uce.tutorias.application.dto.AgendarTutoriaRequest;
import ec.edu.uce.tutorias.domain.exception.DomainException;
import ec.edu.uce.tutorias.domain.model.Disponibilidad;
import ec.edu.uce.tutorias.domain.model.Tutoria;
import ec.edu.uce.tutorias.domain.repository.DisponibilidadRepository;
import ec.edu.uce.tutorias.domain.repository.MateriaRepository;
import ec.edu.uce.tutorias.domain.repository.TutoriaRepository;
import ec.edu.uce.tutorias.domain.repository.UsuarioRepository;
import ec.edu.uce.tutorias.domain.service.ValidadorAgendaTutorias;
import ec.edu.uce.tutorias.domain.vo.MotivoTutoria;
import ec.edu.uce.tutorias.domain.vo.Rol;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AgendarTutoriaUseCase {

    private final TutoriaRepository tutoriaRepository;
    private final DisponibilidadRepository disponibilidadRepository;
    private final UsuarioRepository usuarioRepository;
    private final MateriaRepository materiaRepository;
    private final ValidadorAgendaTutorias validador;

    public AgendarTutoriaUseCase(TutoriaRepository tutoriaRepository,
                                 DisponibilidadRepository disponibilidadRepository,
                                 UsuarioRepository usuarioRepository,
                                 MateriaRepository materiaRepository) {
        this.tutoriaRepository = tutoriaRepository;
        this.disponibilidadRepository = disponibilidadRepository;
        this.usuarioRepository = usuarioRepository;
        this.materiaRepository = materiaRepository;
        this.validador = new ValidadorAgendaTutorias(tutoriaRepository);
    }

    @Transactional
    public String ejecutar(AgendarTutoriaRequest request) {
        // Validar estudiante
        var estudiante = usuarioRepository.buscarPorId(request.getEstudianteId())
                .orElseThrow(() -> new DomainException("Estudiante no encontrado"));
        if (estudiante.getRol() != Rol.ESTUDIANTE || !estudiante.isActivo()) {
            throw new DomainException("El usuario debe ser un estudiante activo");
        }

        // Validar docente
        var docente = usuarioRepository.buscarPorId(request.getDocenteId())
                .orElseThrow(() -> new DomainException("Docente no encontrado"));
        if (docente.getRol() != Rol.DOCENTE || !docente.isActivo()) {
            throw new DomainException("El docente no es válido o está inactivo");
        }

        // Validar materia
        materiaRepository.buscarPorId(request.getMateriaId())
                .orElseThrow(() -> new DomainException("Materia no encontrada"));

        // Validar disponibilidad
        Disponibilidad disponibilidad = disponibilidadRepository.buscarPorId(request.getDisponibilidadId())
                .orElseThrow(() -> new DomainException("Disponibilidad no encontrada"));
        
        if (!disponibilidad.getDocenteId().equals(docente.getId())) {
            throw new DomainException("La disponibilidad no pertenece al docente seleccionado");
        }

        // Reservar disponibilidad (lanza excepción si ya está reservada)
        disponibilidad.reservar();
        disponibilidadRepository.guardar(disponibilidad);

        // Crear tutoría
        Tutoria nuevaTutoria = new Tutoria(
                UUID.randomUUID().toString(),
                estudiante.getId(),
                docente.getId(),
                request.getMateriaId(),
                disponibilidad.getId(),
                request.getFechaHoraInicio(),
                request.getFechaHoraFin(),
                new MotivoTutoria(request.getMotivo())
        );

        // Validar reglas de negocio con el Domain Service (solapamientos, duplicidad semanal)
        validador.validarAgendamiento(nuevaTutoria);

        // Guardar
        tutoriaRepository.guardar(nuevaTutoria);

        return nuevaTutoria.getId();
    }
}
