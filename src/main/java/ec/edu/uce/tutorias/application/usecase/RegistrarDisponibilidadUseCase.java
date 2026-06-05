package ec.edu.uce.tutorias.application.usecase;

import ec.edu.uce.tutorias.application.dto.RegistrarDisponibilidadRequest;
import ec.edu.uce.tutorias.domain.exception.DomainException;
import ec.edu.uce.tutorias.domain.model.Disponibilidad;
import ec.edu.uce.tutorias.domain.repository.DisponibilidadRepository;
import ec.edu.uce.tutorias.domain.repository.MateriaRepository;
import ec.edu.uce.tutorias.domain.repository.UsuarioRepository;
import ec.edu.uce.tutorias.domain.vo.RangoHorario;
import ec.edu.uce.tutorias.domain.vo.Rol;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class RegistrarDisponibilidadUseCase {

    private final DisponibilidadRepository disponibilidadRepository;
    private final UsuarioRepository usuarioRepository;
    private final MateriaRepository materiaRepository;

    public RegistrarDisponibilidadUseCase(DisponibilidadRepository disponibilidadRepository, 
                                          UsuarioRepository usuarioRepository, 
                                          MateriaRepository materiaRepository) {
        this.disponibilidadRepository = disponibilidadRepository;
        this.usuarioRepository = usuarioRepository;
        this.materiaRepository = materiaRepository;
    }

    @Transactional
    public String ejecutar(RegistrarDisponibilidadRequest request) {
        // Validar docente
        var docente = usuarioRepository.buscarPorId(request.getDocenteId())
                .orElseThrow(() -> new DomainException("Docente no encontrado"));
        if (docente.getRol() != Rol.DOCENTE || !docente.isActivo()) {
            throw new DomainException("El usuario debe ser un docente activo");
        }

        // Validar materia
        materiaRepository.buscarPorId(request.getMateriaId())
                .orElseThrow(() -> new DomainException("Materia no encontrada"));

        RangoHorario nuevoRango = new RangoHorario(request.getHoraInicio(), request.getHoraFin());

        // Validar solapamiento de horarios para el mismo docente en la misma fecha
        List<Disponibilidad> disponibilidadesExistentes = disponibilidadRepository.buscarPorDocenteYFecha(
                request.getDocenteId(), 
                request.getFecha()
        );

        for (Disponibilidad d : disponibilidadesExistentes) {
            if (d.getRangoHorario().seSolapaCon(nuevoRango)) {
                throw new DomainException("El horario ingresado se solapa con otra disponibilidad existente");
            }
        }

        Disponibilidad nuevaDisponibilidad = new Disponibilidad(
                UUID.randomUUID().toString(),
                request.getDocenteId(),
                request.getMateriaId(),
                request.getFecha(),
                nuevoRango
        );

        disponibilidadRepository.guardar(nuevaDisponibilidad);

        return nuevaDisponibilidad.getId();
    }
}
