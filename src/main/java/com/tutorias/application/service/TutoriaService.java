package com.tutorias.application.service;

import com.tutorias.domain.model.*;
import com.tutorias.domain.valueobject.*;
import com.tutorias.domain.enums.EstadoTutoria;
import com.tutorias.application.validator.TutoriaValidator;
import com.tutorias.infrastructure.repository.TutoriaRepository;
import com.tutorias.infrastructure.repository.DisponibilidadRepository;
import com.tutorias.infrastructure.repository.UsuarioRepository;
import com.tutorias.infrastructure.notification.NotificationService;
import com.tutorias.domain.exception.DomainException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TutoriaService {
    private final TutoriaRepository tutoriaRepo;
    private final DisponibilidadRepository disponibilidadRepo;
    private final UsuarioRepository usuarioRepo;
    private final HistorialService historialService;
    private final NotificationService notificationService;
    private final TutoriaValidator tutoriaValidator;

    public TutoriaService(TutoriaRepository tutoriaRepo,
                          DisponibilidadRepository disponibilidadRepo,
                          UsuarioRepository usuarioRepo,
                          HistorialService historialService,
                          NotificationService notificationService,
                          TutoriaValidator tutoriaValidator) {
        this.tutoriaRepo = tutoriaRepo;
        this.disponibilidadRepo = disponibilidadRepo;
        this.usuarioRepo = usuarioRepo;
        this.historialService = historialService;
        this.notificationService = notificationService;
        this.tutoriaValidator = tutoriaValidator;
    }

    public List<Tutoria> listarTutorias(String estudianteId, String docenteId, String estado, 
                                        String estudianteNombre, String docenteNombre) {
        return tutoriaRepo.findByFiltros(estudianteId, docenteId, estado, estudianteNombre, docenteNombre);
    }

    public List<HistorialTutoria> obtenerHistorial(String tutoriaId) {
        return historialService.obtenerHistorialPorTutoriaId(tutoriaId);
    }

    public Tutoria agendarTutoria(String estudianteId, String disponibilidadId, String motivoStr) {
        LocalDateTime ahora = LocalDateTime.now();

        // 1. Validar existencia del estudiante y que esté activo
        Estudiante estudiante = usuarioRepo.findEstudianteById(estudianteId)
                .orElseThrow(() -> new DomainException("El estudiante no existe"));
        Usuario estUsuario = usuarioRepo.findById(estudiante.getUsuarioId())
                .orElseThrow(() -> new DomainException("Usuario del estudiante no encontrado"));
        estUsuario.verificarAcceso();

        // 2. Validar disponibilidad
        Disponibilidad disp = disponibilidadRepo.findById(disponibilidadId)
                .orElseThrow(() -> new DomainException("El horario seleccionado no existe o ya no esta disponible"));
        
        disp.reservar();

        LocalDateTime fechaHoraInicio = LocalDateTime.of(disp.getFecha(), disp.getHoraInicio());
        LocalDateTime fechaHoraFin = LocalDateTime.of(disp.getFecha(), disp.getHoraFin());

        // 3. Regla: No agendar en fecha pasada
        if (fechaHoraInicio.isBefore(ahora)) {
            throw new DomainException("No se puede agendar una tutoría en el pasado");
        }

        // 4. Regla: Duplicidad semanal (validador de aplicación)
        tutoriaValidator.validarDuplicidadSemanal(estudianteId, disp.getDocenteId(), disp.getMateriaId(), disp.getFecha());

        // 5. Regla: No solapamiento de horarios del estudiante (validador de aplicación)
        tutoriaValidator.validarSolapamientoEstudiante(estudianteId, fechaHoraInicio, fechaHoraFin);

        // 6. Persistir cambios de disponibilidad
        disponibilidadRepo.save(disp);

        // 7. Crear tutoría
        String tutoriaId = UUID.randomUUID().toString();
        Tutoria tutoria = new Tutoria(
                tutoriaId,
                estudianteId,
                estUsuario.getNombre() + " " + estUsuario.getApellido(),
                disp.getDocenteId(),
                disp.getDocenteNombre(),
                disp.getMateriaId(),
                disp.getMateriaNombre(),
                disponibilidadId,
                fechaHoraInicio,
                fechaHoraFin,
                new MotivoTutoria(motivoStr),
                EstadoTutoria.PENDIENTE,
                ahora,
                ahora
        );

        Tutoria savedTutoria = tutoriaRepo.save(tutoria);

        // 8. Registrar Historial
        historialService.registrar(tutoriaId, null, EstadoTutoria.PENDIENTE.getValor(), 
                "Creacion de solicitud de tutoria academica", ahora, estUsuario.getCorreoInstitucional().value());

        // 9. Enviar Notificación
        notificationService.enviarNotificacion(tutoriaId, estUsuario.getCorreoInstitucional().value(), "creacion",
                "Tu tutoria de " + disp.getMateriaNombre() + " con el Doc. " + disp.getDocenteNombre() + " el " + disp.getFecha() + " a las " + disp.getHoraInicio() + " ha sido solicitada. Estado: Pendiente.");

        return savedTutoria;
    }

    public Tutoria cambiarEstado(String id, String nuevoEstadoStr, String observacion, String usuarioResponsableEmail) {
        LocalDateTime ahora = LocalDateTime.now();
        Tutoria tutoria = tutoriaRepo.findById(id)
                .orElseThrow(() -> new DomainException("Tutoria no encontrada"));

        EstadoTutoria estadoAnterior = tutoria.getEstado();
        EstadoTutoria nuevoEstado = EstadoTutoria.fromString(nuevoEstadoStr);

        // Buscar rol del usuario responsable para las reglas de cancelación
        Usuario uResp = usuarioRepo.findByCorreoInstitucional(usuarioResponsableEmail)
                .orElseThrow(() -> new DomainException("Usuario responsable no encontrado"));
        String rolUsuario = uResp.getRol().getValor();

        // Aplicar transiciones en el dominio
        switch (nuevoEstado) {
            case CONFIRMADA:
                tutoria.confirmar(ahora);
                break;
            case ASISTIDA:
                tutoria.registrarAsistencia(observacion, ahora);
                break;
            case INASISTENCIA:
                tutoria.registrarInasistencia(observacion, ahora);
                break;
            case CANCELADA:
                tutoria.cancelar(rolUsuario, ahora);
                
                // Liberar disponibilidad
                Disponibilidad disp = disponibilidadRepo.findById(tutoria.getDisponibilidadId())
                        .orElseThrow(() -> new DomainException("Disponibilidad asociada no encontrada"));
                disp.liberar();
                disponibilidadRepo.save(disp);
                break;
            default:
                throw new DomainException("Transicion de estado no soportada: " + nuevoEstadoStr);
        }

        Tutoria savedTutoria = tutoriaRepo.save(tutoria);

        // Historial
        historialService.registrar(id, estadoAnterior.getValor(), nuevoEstado.getValor(), 
                observacion != null ? observacion : "Cambio de estado a " + nuevoEstado.getValor(), ahora, usuarioResponsableEmail);

        // Notificación al estudiante
        Estudiante estudiante = usuarioRepo.findEstudianteById(tutoria.getEstudianteId())
                .orElseThrow(() -> new DomainException("Estudiante no encontrado"));
        Usuario uEst = usuarioRepo.findById(estudiante.getUsuarioId())
                .orElseThrow(() -> new DomainException("Usuario del estudiante no encontrado"));

        notificationService.enviarNotificacion(id, uEst.getCorreoInstitucional().value(), "cambio_estado",
                "Tu tutoria de " + tutoria.getMateriaNombre() + " cambio de estado a: " + nuevoEstado.getValor() + ". Detalle: " + observacion);

        return savedTutoria;
    }
}
