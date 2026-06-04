package com.tutorias.infrastructure.repository.mongo.mapper;

import com.tutorias.domain.model.*;
import com.tutorias.domain.valueobject.*;
import com.tutorias.domain.enums.*;
import com.tutorias.infrastructure.repository.mongo.document.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PersistenciaMapper {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter SECONDS_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Usuario mapping
    public static Usuario toDomain(UsuarioDocument doc) {
        if (doc == null) return null;
        LocalDateTime fecha = doc.getFechaCreacion() != null ? LocalDateTime.parse(doc.getFechaCreacion(), SECONDS_FORMATTER) : null;
        return new Usuario(
            doc.getId(),
            doc.getNombre(),
            doc.getApellido(),
            new CorreoInstitucional(doc.getCorreoInstitucional()),
            doc.getContrasena(),
            RolUsuario.fromString(doc.getRol()),
            EstadoUsuario.fromString(doc.getEstado()),
            fecha
        );
    }

    public static UsuarioDocument toDocument(Usuario domain) {
        if (domain == null) return null;
        UsuarioDocument doc = new UsuarioDocument();
        doc.setId(domain.getId());
        doc.setNombre(domain.getNombre());
        doc.setApellido(domain.getApellido());
        doc.setCorreoInstitucional(domain.getCorreoInstitucional().value());
        doc.setContrasena(domain.getContrasena());
        doc.setRol(domain.getRol().getValor());
        doc.setEstado(domain.getEstado().getValor());
        doc.setFechaCreacion(domain.getFechaCreacion() != null ? domain.getFechaCreacion().format(SECONDS_FORMATTER) : null);
        return doc;
    }

    // Estudiante mapping
    public static Estudiante toDomain(EstudianteDocument doc) {
        if (doc == null) return null;
        return new Estudiante(doc.getId(), doc.getUsuarioId(), doc.getCarrera(), doc.getNivel(), doc.getParalelo());
    }

    public static EstudianteDocument toDocument(Estudiante domain) {
        if (domain == null) return null;
        EstudianteDocument doc = new EstudianteDocument();
        doc.setId(domain.getId());
        doc.setUsuarioId(domain.getUsuarioId());
        doc.setCarrera(domain.getCarrera());
        doc.setNivel(domain.getNivel());
        doc.setParalelo(domain.getParalelo());
        return doc;
    }

    // Docente mapping
    public static Docente toDomain(DocenteDocument doc) {
        if (doc == null) return null;
        return new Docente(doc.getId(), doc.getUsuarioId(), doc.getDepartamento());
    }

    public static DocenteDocument toDocument(Docente domain) {
        if (domain == null) return null;
        DocenteDocument doc = new DocenteDocument();
        doc.setId(domain.getId());
        doc.setUsuarioId(domain.getUsuarioId());
        doc.setDepartamento(domain.getDepartamento());
        return doc;
    }

    // Materia mapping
    public static Materia toDomain(MateriaDocument doc) {
        if (doc == null) return null;
        return new Materia(doc.getId(), doc.getCodigo(), doc.getNombre(), doc.getEstado());
    }

    public static MateriaDocument toDocument(Materia domain) {
        if (domain == null) return null;
        MateriaDocument doc = new MateriaDocument();
        doc.setId(domain.getId());
        doc.setCodigo(domain.getCodigo());
        doc.setNombre(domain.getNombre());
        doc.setEstado(domain.getEstado());
        return doc;
    }

    // Disponibilidad mapping
    public static Disponibilidad toDomain(DisponibilidadDocument doc) {
        if (doc == null) return null;
        LocalDate fecha = doc.getFecha() != null ? LocalDate.parse(doc.getFecha(), DATE_FORMATTER) : null;
        LocalTime inicio = doc.getHoraInicio() != null ? LocalTime.parse(doc.getHoraInicio(), TIME_FORMATTER) : null;
        LocalTime fin = doc.getHoraFin() != null ? LocalTime.parse(doc.getHoraFin(), TIME_FORMATTER) : null;
        return new Disponibilidad(
            doc.getId(),
            doc.getDocenteId(),
            doc.getDocenteNombre(),
            doc.getMateriaId(),
            doc.getMateriaNombre(),
            fecha,
            inicio,
            fin,
            doc.getEstado()
        );
    }

    public static DisponibilidadDocument toDocument(Disponibilidad domain) {
        if (domain == null) return null;
        DisponibilidadDocument doc = new DisponibilidadDocument();
        doc.setId(domain.getId());
        doc.setDocenteId(domain.getDocenteId());
        doc.setDocenteNombre(domain.getDocenteNombre());
        doc.setMateriaId(domain.getMateriaId());
        doc.setMateriaNombre(domain.getMateriaNombre());
        doc.setFecha(domain.getFecha() != null ? domain.getFecha().format(DATE_FORMATTER) : null);
        doc.setHoraInicio(domain.getHoraInicio() != null ? domain.getHoraInicio().format(TIME_FORMATTER) : null);
        doc.setHoraFin(domain.getHoraFin() != null ? domain.getHoraFin().format(TIME_FORMATTER) : null);
        doc.setEstado(domain.getEstado());
        return doc;
    }

    // Tutoria mapping
    public static Tutoria toDomain(TutoriaDocument doc) {
        if (doc == null) return null;
        LocalDateTime inicio = doc.getFechaHoraInicio() != null ? LocalDateTime.parse(doc.getFechaHoraInicio(), DATETIME_FORMATTER) : null;
        LocalDateTime fin = doc.getFechaHoraFin() != null ? LocalDateTime.parse(doc.getFechaHoraFin(), DATETIME_FORMATTER) : null;
        LocalDateTime creacion = doc.getFechaCreacion() != null ? LocalDateTime.parse(doc.getFechaCreacion(), SECONDS_FORMATTER) : null;
        LocalDateTime actualizacion = doc.getFechaActualizacion() != null ? LocalDateTime.parse(doc.getFechaActualizacion(), SECONDS_FORMATTER) : null;
        
        Tutoria tut = new Tutoria(
            doc.getId(),
            doc.getEstudianteId(),
            doc.getEstudianteNombre(),
            doc.getDocenteId(),
            doc.getDocenteNombre(),
            doc.getMateriaId(),
            doc.getMateriaNombre(),
            doc.getDisponibilidadId(),
            inicio,
            fin,
            new MotivoTutoria(doc.getMotivo()),
            EstadoTutoria.fromString(doc.getEstado()),
            creacion,
            actualizacion
        );
        tut.setObservacion(doc.getObservacion());
        return tut;
    }

    public static TutoriaDocument toDocument(Tutoria domain) {
        if (domain == null) return null;
        TutoriaDocument doc = new TutoriaDocument();
        doc.setId(domain.getId());
        doc.setEstudianteId(domain.getEstudianteId());
        doc.setEstudianteNombre(domain.getEstudianteNombre());
        doc.setDocenteId(domain.getDocenteId());
        doc.setDocenteNombre(domain.getDocenteNombre());
        doc.setMateriaId(domain.getMateriaId());
        doc.setMateriaNombre(domain.getMateriaNombre());
        doc.setDisponibilidadId(domain.getDisponibilidadId());
        doc.setFechaHoraInicio(domain.getFechaHoraInicio() != null ? domain.getFechaHoraInicio().format(DATETIME_FORMATTER) : null);
        doc.setFechaHoraFin(domain.getFechaHoraFin() != null ? domain.getFechaHoraFin().format(DATETIME_FORMATTER) : null);
        doc.setMotivo(domain.getMotivo().value());
        doc.setEstado(domain.getEstado().getValor());
        doc.setFechaCreacion(domain.getFechaCreacion() != null ? domain.getFechaCreacion().format(SECONDS_FORMATTER) : null);
        doc.setFechaActualizacion(domain.getFechaActualizacion() != null ? domain.getFechaActualizacion().format(SECONDS_FORMATTER) : null);
        doc.setObservacion(domain.getObservacion());
        return doc;
    }

    // Historial mapping
    public static HistorialTutoria toDomain(HistorialTutoriaDocument doc) {
        if (doc == null) return null;
        LocalDateTime fecha = doc.getFechaEvento() != null ? LocalDateTime.parse(doc.getFechaEvento(), SECONDS_FORMATTER) : null;
        return new HistorialTutoria(
            doc.getId(),
            doc.getTutoriaId(),
            doc.getEstadoAnterior(),
            doc.getEstadoNuevo(),
            doc.getDescripcion(),
            fecha,
            doc.getUsuarioResponsable()
        );
    }

    public static HistorialTutoriaDocument toDocument(HistorialTutoria domain) {
        if (domain == null) return null;
        HistorialTutoriaDocument doc = new HistorialTutoriaDocument();
        doc.setId(domain.getId());
        doc.setTutoriaId(domain.getTutoriaId());
        doc.setEstadoAnterior(domain.getEstadoAnterior());
        doc.setEstadoNuevo(domain.getEstadoNuevo());
        doc.setDescripcion(domain.getDescripcion());
        doc.setFechaEvento(domain.getFechaEvento() != null ? domain.getFechaEvento().format(SECONDS_FORMATTER) : null);
        doc.setUsuarioResponsable(domain.getUsuarioResponsable());
        return doc;
    }

    // Notificacion mapping
    public static Notificacion toDomain(NotificacionDocument doc) {
        if (doc == null) return null;
        LocalDateTime fecha = doc.getFechaEnvio() != null ? LocalDateTime.parse(doc.getFechaEnvio(), SECONDS_FORMATTER) : null;
        return new Notificacion(
            doc.getId(),
            doc.getTutoriaId(),
            doc.getDestinatario(),
            doc.getTipo(),
            doc.getMensaje(),
            doc.getEstadoEnvio(),
            fecha
        );
    }

    public static NotificacionDocument toDocument(Notificacion domain) {
        if (domain == null) return null;
        NotificacionDocument doc = new NotificacionDocument();
        doc.setId(domain.getId());
        doc.setTutoriaId(domain.getTutoriaId());
        doc.setDestinatario(domain.getDestinatario());
        doc.setTipo(domain.getTipo());
        doc.setMensaje(domain.getMensaje());
        doc.setEstadoEnvio(domain.getEstadoEnvio());
        doc.setFechaEnvio(domain.getFechaEnvio() != null ? domain.getFechaEnvio().format(SECONDS_FORMATTER) : null);
        return doc;
    }
}
