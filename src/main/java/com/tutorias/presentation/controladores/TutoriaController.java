package com.tutorias.presentation.controladores;

import com.tutorias.application.servicios.TutoriaApplicationService;
import com.tutorias.application.servicios.UsuarioApplicationService;
import com.tutorias.domain.entities.*;
import com.tutorias.domain.enums.EstadoTutoria;
import com.tutorias.domain.repositorios.MateriaRepository;
import com.tutorias.presentation.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tutorias")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Tutorías", description = "Operaciones de agendamiento, confirmación, asistencia y cancelación de tutorías")
public class TutoriaController {

    private final TutoriaApplicationService tutoriaApplicationService;
    private final UsuarioApplicationService usuarioApplicationService;
    private final MateriaRepository materiaRepository;

    @PostMapping
    @Operation(summary = "Solicitar una tutoría académica (Estudiantes)")
    public TutoriaResponse solicitar(@Valid @RequestBody TutoriaRequest request) {
        Tutoria tutoria = tutoriaApplicationService.solicitarTutoria(
                request.getEstudianteId(),
                request.getDocenteId(),
                request.getMateriaId(),
                request.getDisponibilidadId(),
                request.getMotivo()
        );
        return mapToResponse(tutoria);
    }

    @PutMapping("/{id}/confirmar")
    @Operation(summary = "Confirmar formalmente una tutoría (Docente)")
    public TutoriaResponse confirmar(
            @PathVariable String id,
            @RequestHeader("X-Usuario-Nombre") String docenteNombre) {
        Tutoria tutoria = tutoriaApplicationService.confirmarTutoria(id, docenteNombre);
        return mapToResponse(tutoria);
    }

    @PutMapping("/{id}/asistencia")
    @Operation(summary = "Registrar asistencia del estudiante (Docente)")
    public TutoriaResponse registrarAsistencia(
            @PathVariable String id,
            @RequestHeader("X-Usuario-Nombre") String docenteNombre) {
        Tutoria tutoria = tutoriaApplicationService.registrarAsistencia(id, docenteNombre);
        return mapToResponse(tutoria);
    }

    @PutMapping("/{id}/inasistencia")
    @Operation(summary = "Registrar inasistencia del estudiante (Docente)")
    public TutoriaResponse registrarInasistencia(
            @PathVariable String id,
            @RequestHeader("X-Usuario-Nombre") String docenteNombre) {
        Tutoria tutoria = tutoriaApplicationService.registrarInasistencia(id, docenteNombre);
        return mapToResponse(tutoria);
    }

    @PutMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar una tutoría con al menos 24 horas de antelación (Estudiante)")
    public TutoriaResponse cancelar(
            @PathVariable String id,
            @RequestHeader("X-Usuario-Nombre") String estudianteNombre) {
        Tutoria tutoria = tutoriaApplicationService.cancelarTutoria(id, estudianteNombre);
        return mapToResponse(tutoria);
    }

    @GetMapping("/estudiante/{estudianteId}")
    @Operation(summary = "Listar la agenda de tutorías del estudiante")
    public List<TutoriaResponse> listarPorEstudiante(@PathVariable String estudianteId) {
        return tutoriaApplicationService.listarTutoriasEstudiante(estudianteId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/docente/{docenteId}")
    @Operation(summary = "Listar la agenda de tutorías del docente")
    public List<TutoriaResponse> listarPorDocente(@PathVariable String docenteId) {
        return tutoriaApplicationService.listarTutoriasDocente(docenteId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping
    @Operation(summary = "Filtrar y listar tutorías de todo el sistema por estudiante, docente, estado y fecha (RF-25)")
    public List<TutoriaResponse> filtrar(
            @RequestParam(required = false) String estudianteId,
            @RequestParam(required = false) String docenteId,
            @RequestParam(required = false) EstadoTutoria estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        return tutoriaApplicationService.listarTodas().stream()
                .filter(t -> estudianteId == null || t.getEstudianteId().equals(estudianteId))
                .filter(t -> docenteId == null || t.getDocenteId().equals(docenteId))
                .filter(t -> estado == null || t.getEstado() == estado)
                .filter(t -> fecha == null || t.getFechaHora().fecha().equals(fecha))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/historial")
    @Operation(summary = "Consultar el historial detallado de cambios de una tutoría")
    public List<HistorialTutoriaResponse> verHistorial(@PathVariable String id) {
        return tutoriaApplicationService.obtenerHistorialPorTutoria(id).stream()
                .map(h -> new HistorialTutoriaResponse(
                        h.getId(),
                        h.getTutoriaId(),
                        h.getEstadoAnterior(),
                        h.getEstadoNuevo(),
                        h.getFechaCambio(),
                        h.getModificadoPor(),
                        h.getDetalle()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/notificaciones/{usuarioId}")
    @Operation(summary = "Listar el buzón de notificaciones de un usuario")
    public List<NotificacionResponse> verNotificaciones(@PathVariable String usuarioId) {
        return tutoriaApplicationService.obtenerNotificacionesPorUsuario(usuarioId).stream()
                .map(n -> new NotificacionResponse(
                        n.getId(),
                        n.getUsuarioId(),
                        n.getMensaje(),
                        n.getFechaCreacion(),
                        n.isLeido()
                ))
                .collect(Collectors.toList());
    }

    private TutoriaResponse mapToResponse(Tutoria t) {
        String estudianteNombre = "Estudiante Desconocido";
        String docenteNombre = "Docente Desconocido";
        String materiaNombre = "Materia Desconocida";

        try {
            Usuario est = usuarioApplicationService.buscarPorId(t.getEstudianteId());
            estudianteNombre = est.getNombre();
        } catch (Exception ignored) {}

        try {
            Usuario doc = usuarioApplicationService.buscarPorId(t.getDocenteId());
            docenteNombre = doc.getNombre();
        } catch (Exception ignored) {}

        try {
            Materia mat = materiaRepository.findById(t.getMateriaId()).orElse(null);
            if (mat != null) {
                materiaNombre = mat.getNombre();
            }
        } catch (Exception ignored) {}

        return new TutoriaResponse(
                t.getId(),
                t.getEstudianteId(),
                estudianteNombre,
                t.getDocenteId(),
                docenteNombre,
                t.getMateriaId(),
                materiaNombre,
                t.getFechaHora().fecha(),
                t.getFechaHora().rangoHorario().horaInicio(),
                t.getFechaHora().rangoHorario().horaFin(),
                t.getMotivo().valor(),
                t.getEstado()
        );
    }
}
