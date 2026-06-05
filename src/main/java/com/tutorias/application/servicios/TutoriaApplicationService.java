package com.tutorias.application.servicios;

import com.tutorias.domain.entities.*;
import com.tutorias.domain.enums.EstadoTutoria;
import com.tutorias.domain.enums.RolUsuario;
import com.tutorias.domain.exceptions.DomainException;
import com.tutorias.domain.repositorios.*;
import com.tutorias.domain.services.ValidadorAgendaTutorias;
import com.tutorias.domain.valueobjects.FechaHoraTutoria;
import com.tutorias.domain.valueobjects.MotivoTutoria;
import com.tutorias.domain.valueobjects.RangoHorario;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TutoriaApplicationService {

    private final TutoriaRepository tutoriaRepository;
    private final DisponibilidadRepository disponibilidadRepository;
    private final UsuarioRepository usuarioRepository;
    private final MateriaRepository materiaRepository;
    private final HistorialTutoriaRepository historialTutoriaRepository;
    private final NotificacionRepository notificacionRepository;
    private final ValidadorAgendaTutorias validador = new ValidadorAgendaTutorias();

    public Tutoria solicitarTutoria(String estudianteId, String docenteId, String materiaId, String disponibilidadId, String motivoTexto) {
        // 1. Validations on Students & Teachers Active state
        Usuario estudianteUser = usuarioRepository.findById(estudianteId)
                .orElseThrow(() -> new DomainException("Estudiante no encontrado."));
        if (!estudianteUser.isActivo() || estudianteUser.getRol() != RolUsuario.ESTUDIANTE) {
            throw new DomainException("Solo un estudiante activo puede solicitar una tutoría.");
        }

        Usuario docenteUser = usuarioRepository.findById(docenteId)
                .orElseThrow(() -> new DomainException("Docente no encontrado."));
        if (!docenteUser.isActivo() || docenteUser.getRol() != RolUsuario.DOCENTE) {
            throw new DomainException("El docente seleccionado no está activo o no es válido.");
        }

        // 2. Validate Subject
        Materia materia = materiaRepository.findById(materiaId)
                .orElseThrow(() -> new DomainException("La materia seleccionada no existe."));

        // 3. Validate Availability
        Disponibilidad disponibilidad = disponibilidadRepository.findById(disponibilidadId)
                .orElseThrow(() -> new DomainException("La disponibilidad seleccionada no existe."));

        if (!disponibilidad.getDocenteId().equals(docenteId)) {
            throw new DomainException("La disponibilidad no corresponde al docente seleccionado.");
        }

        if (disponibilidad.isReservada()) {
            throw new DomainException("La disponibilidad ya está reservada por otra tutoría.");
        }

        // 4. Validate date rules (not in the past)
        FechaHoraTutoria fechaHora = new FechaHoraTutoria(
                disponibilidad.getFecha(),
                disponibilidad.getRangoHorario()
        );
        fechaHora.validarFechaNoPasada(LocalDate.now());

        // 5. Build Motivo Value Object
        MotivoTutoria motivo = new MotivoTutoria(motivoTexto);

        // 6. Cross-validation: weekly duplicates, overlapping hours for student and docent
        List<Tutoria> todasTutorias = tutoriaRepository.findAll();

        validador.validarNoDuplicidadSemanal(estudianteId, docenteId, materiaId, disponibilidad.getFecha(), todasTutorias);
        validador.validarNoCruceEstudiante(estudianteId, disponibilidad.getFecha(), disponibilidad.getRangoHorario(), todasTutorias);
        validador.validarNoCruceDocente(docenteId, disponibilidad.getFecha(), disponibilidad.getRangoHorario(), todasTutorias);

        // 7. Make reservations & Create Tutoria
        disponibilidad.reservar();
        disponibilidadRepository.save(disponibilidad);

        Tutoria tutoria = new Tutoria(
                null,
                estudianteId,
                docenteId,
                materiaId,
                fechaHora,
                motivo,
                EstadoTutoria.PENDIENTE,
                disponibilidadId
        );
        Tutoria savedTutoria = tutoriaRepository.save(tutoria);

        // 8. Write audit change to history
        registrarHistorial(savedTutoria.getId(), null, EstadoTutoria.PENDIENTE, estudianteUser.getNombre(),
                "Registro inicial de la solicitud de tutoría realizada por el estudiante.");

        // 9. Dispatch notifications
        registrarNotificacion(estudianteId, String.format(
                "Has solicitado una tutoría de %s con el docente %s para el %s en el horario %s - %s.",
                materia.getNombre(), docenteUser.getNombre(), disponibilidad.getFecha(),
                disponibilidad.getRangoHorario().horaInicio(), disponibilidad.getRangoHorario().horaFin()));

        registrarNotificacion(docenteId, String.format(
                "El estudiante %s ha solicitado una tutoría de %s para el %s en el horario %s - %s.",
                estudianteUser.getNombre(), materia.getNombre(), disponibilidad.getFecha(),
                disponibilidad.getRangoHorario().horaInicio(), disponibilidad.getRangoHorario().horaFin()));

        return savedTutoria;
    }

    public Tutoria confirmarTutoria(String tutoriaId, String modificadoPor) {
        Tutoria tutoria = tutoriaRepository.findById(tutoriaId)
                .orElseThrow(() -> new DomainException("Tutoría no encontrada."));

        EstadoTutoria anterior = tutoria.getEstado();
        tutoria.confirmar();
        Tutoria saved = tutoriaRepository.save(tutoria);

        registrarHistorial(saved.getId(), anterior, EstadoTutoria.CONFIRMADA, modificadoPor,
                "El docente ha verificado y confirmado formalmente la tutoría.");

        registrarNotificacion(tutoria.getEstudianteId(), "Su tutoría académica ha sido CONFIRMADA por el docente.");

        return saved;
    }

    public Tutoria registrarAsistencia(String tutoriaId, String modificadoPor) {
        Tutoria tutoria = tutoriaRepository.findById(tutoriaId)
                .orElseThrow(() -> new DomainException("Tutoría no encontrada."));

        EstadoTutoria anterior = tutoria.getEstado();
        tutoria.marcarAsistencia();
        Tutoria saved = tutoriaRepository.save(tutoria);

        registrarHistorial(saved.getId(), anterior, EstadoTutoria.ASISTIDA, modificadoPor,
                "Se registró la asistencia satisfactoria del estudiante.");

        registrarNotificacion(tutoria.getEstudianteId(), "Se ha marcado su asistencia a la tutoría.");

        return saved;
    }

    public Tutoria registrarInasistencia(String tutoriaId, String modificadoPor) {
        Tutoria tutoria = tutoriaRepository.findById(tutoriaId)
                .orElseThrow(() -> new DomainException("Tutoría no encontrada."));

        EstadoTutoria anterior = tutoria.getEstado();
        tutoria.marcarInasistencia();
        Tutoria saved = tutoriaRepository.save(tutoria);

        registrarHistorial(saved.getId(), anterior, EstadoTutoria.INASISTENCIA, modificadoPor,
                "Se registró la inasistencia del estudiante a la tutoría.");

        registrarNotificacion(tutoria.getEstudianteId(), "Se ha marcado inasistencia para su tutoría.");

        return saved;
    }

    public Tutoria cancelarTutoria(String tutoriaId, String modificadoPor) {
        Tutoria tutoria = tutoriaRepository.findById(tutoriaId)
                .orElseThrow(() -> new DomainException("Tutoría no encontrada."));

        EstadoTutoria anterior = tutoria.getEstado();
        // Check 24-hour limit relative to now
        tutoria.cancelar(LocalDateTime.now());
        Tutoria saved = tutoriaRepository.save(tutoria);

        // Release availability
        if (tutoria.getDisponibilidadId() != null) {
            disponibilidadRepository.findById(tutoria.getDisponibilidadId()).ifPresent(disp -> {
                disp.liberar();
                disponibilidadRepository.save(disp);
            });
        }

        registrarHistorial(saved.getId(), anterior, EstadoTutoria.CANCELADA, modificadoPor,
                "Tutoría cancelada correctamente. Rango horario liberado.");

        registrarNotificacion(tutoria.getEstudianteId(), "Se ha cancelado la tutoría. El bloque de tiempo ha sido liberado.");
        registrarNotificacion(tutoria.getDocenteId(), "La tutoría agendada ha sido cancelada por el estudiante.");

        return saved;
    }

    public List<Tutoria> listarTutoriasEstudiante(String estudianteId) {
        return tutoriaRepository.findByEstudianteId(estudianteId);
    }

    public List<Tutoria> listarTutoriasDocente(String docenteId) {
        return tutoriaRepository.findByDocenteId(docenteId);
    }

    public List<Tutoria> listarTodas() {
        return tutoriaRepository.findAll();
    }

    public List<HistorialTutoria> obtenerHistorialPorTutoria(String tutoriaId) {
        return historialTutoriaRepository.findByTutoriaId(tutoriaId);
    }

    public List<Notificacion> obtenerNotificacionesPorUsuario(String usuarioId) {
        return notificacionRepository.findByUsuarioId(usuarioId);
    }

    private void registrarHistorial(String tutoriaId, EstadoTutoria anterior, EstadoTutoria nuevo, String modificadoPor, String detalle) {
        HistorialTutoria h = new HistorialTutoria(
                null,
                tutoriaId,
                anterior,
                nuevo,
                LocalDateTime.now(),
                modificadoPor,
                detalle
        );
        historialTutoriaRepository.save(h);
    }

    private void registrarNotificacion(String usuarioId, String mensaje) {
        Notificacion n = new Notificacion(
                null,
                usuarioId,
                mensaje,
                LocalDateTime.now(),
                false
        );
        notificacionRepository.save(n);
    }
}
