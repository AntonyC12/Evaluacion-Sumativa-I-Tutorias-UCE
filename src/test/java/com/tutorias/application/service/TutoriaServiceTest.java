package com.tutorias.application.service;

import com.tutorias.application.validator.TutoriaValidator;
import com.tutorias.domain.enums.EstadoTutoria;
import com.tutorias.domain.enums.EstadoUsuario;
import com.tutorias.domain.enums.RolUsuario;
import com.tutorias.domain.exception.DomainException;
import com.tutorias.domain.model.*;
import com.tutorias.domain.valueobject.CorreoInstitucional;
import com.tutorias.domain.valueobject.MotivoTutoria;
import com.tutorias.infrastructure.notification.NotificationService;
import com.tutorias.infrastructure.repository.DisponibilidadRepository;
import com.tutorias.infrastructure.repository.TutoriaRepository;
import com.tutorias.infrastructure.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para TutoriaService (capa de aplicación).
 * Usa Mockito para aislar la lógica de orquestación de sus dependencias,
 * demostrando que la capa de aplicación coordina sin acoplar infraestructura.
 */
@ExtendWith(MockitoExtension.class)
public class TutoriaServiceTest {

    @Mock private TutoriaRepository tutoriaRepo;
    @Mock private DisponibilidadRepository disponibilidadRepo;
    @Mock private UsuarioRepository usuarioRepo;
    @Mock private HistorialService historialService;
    @Mock private NotificationService notificationService;
    @Mock private TutoriaValidator tutoriaValidator;

    @InjectMocks
    private TutoriaService tutoriaService;

    private Estudiante estudiante;
    private Usuario usuarioEstudiante;
    private Disponibilidad disponibilidad;
    private LocalDateTime baseTime;

    @BeforeEach
    public void setUp() {
        baseTime = LocalDateTime.now().plusDays(10);

        estudiante = new Estudiante("e1", "u4", "Computacion", "7", "A");

        usuarioEstudiante = new Usuario("u4", "Antony", "Coello",
                new CorreoInstitucional("estudiante1@uce.edu.ec"), "pass123",
                RolUsuario.ESTUDIANTE, EstadoUsuario.ACTIVO, LocalDateTime.now().minusDays(5));

        disponibilidad = new Disponibilidad(
                "disp1", "d1", "Carlos Perez",
                "1", "Arquitectura de Software",
                baseTime.toLocalDate(),
                baseTime.toLocalTime(),
                baseTime.plusHours(1).toLocalTime(),
                "Disponible"
        );
    }

    // ─── agendarTutoria ───────────────────────────────────────────────────────

    @Test
    public void testAgendarTutoriaExitosamente() {
        when(usuarioRepo.findEstudianteById("e1")).thenReturn(Optional.of(estudiante));
        when(usuarioRepo.findById("u4")).thenReturn(Optional.of(usuarioEstudiante));
        when(disponibilidadRepo.findById("disp1")).thenReturn(Optional.of(disponibilidad));
        when(tutoriaRepo.save(any(Tutoria.class))).thenAnswer(inv -> inv.getArgument(0));

        Tutoria resultado = tutoriaService.agendarTutoria("e1", "disp1", "Dudas sobre capas");

        assertNotNull(resultado);
        assertEquals(EstadoTutoria.PENDIENTE, resultado.getEstado());
        assertEquals("e1", resultado.getEstudianteId());
        assertEquals("d1", resultado.getDocenteId());
        assertEquals("Arquitectura de Software", resultado.getMateriaNombre());

        // Verificar que se persiste la disponibilidad actualizada y se llama historial/notif
        verify(disponibilidadRepo, times(1)).save(any(Disponibilidad.class));
        verify(tutoriaRepo, times(1)).save(any(Tutoria.class));
        verify(historialService, times(1)).registrar(
                anyString(), isNull(), eq(EstadoTutoria.PENDIENTE.getValor()),
                anyString(), any(LocalDateTime.class), anyString());
        verify(notificationService, times(1)).enviarNotificacion(
                anyString(), anyString(), eq("creacion"), anyString());
    }

    @Test
    public void testAgendarTutoriaEstudianteNoEncontradoLanzaExcepcion() {
        when(usuarioRepo.findEstudianteById("no-existe")).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () ->
                tutoriaService.agendarTutoria("no-existe", "disp1", "Motivo"));
    }

    @Test
    public void testAgendarTutoriaUsuarioInactivoLanzaExcepcion() {
        Usuario usuarioInactivo = new Usuario("u6", "Inactivo", "Prueba",
                new CorreoInstitucional("inactivo@uce.edu.ec"), "pass",
                RolUsuario.ESTUDIANTE, EstadoUsuario.INACTIVO, LocalDateTime.now());
        Estudiante estInactivo = new Estudiante("e3", "u6", "Computacion", "1", "A");

        when(usuarioRepo.findEstudianteById("e3")).thenReturn(Optional.of(estInactivo));
        when(usuarioRepo.findById("u6")).thenReturn(Optional.of(usuarioInactivo));

        assertThrows(DomainException.class, () ->
                tutoriaService.agendarTutoria("e3", "disp1", "Motivo"));
    }

    @Test
    public void testAgendarTutoriaDisponibilidadNoExisteLanzaExcepcion() {
        when(usuarioRepo.findEstudianteById("e1")).thenReturn(Optional.of(estudiante));
        when(usuarioRepo.findById("u4")).thenReturn(Optional.of(usuarioEstudiante));
        when(disponibilidadRepo.findById("no-existe")).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () ->
                tutoriaService.agendarTutoria("e1", "no-existe", "Motivo"));
    }

    @Test
    public void testAgendarTutoriaDisponibilidadYaReservadaLanzaExcepcion() {
        Disponibilidad dispReservada = new Disponibilidad(
                "disp-res", "d1", "Carlos Perez", "1", "Arquitectura de Software",
                baseTime.toLocalDate(), baseTime.toLocalTime(),
                baseTime.plusHours(1).toLocalTime(), "Reservado");

        when(usuarioRepo.findEstudianteById("e1")).thenReturn(Optional.of(estudiante));
        when(usuarioRepo.findById("u4")).thenReturn(Optional.of(usuarioEstudiante));
        when(disponibilidadRepo.findById("disp-res")).thenReturn(Optional.of(dispReservada));

        assertThrows(DomainException.class, () ->
                tutoriaService.agendarTutoria("e1", "disp-res", "Motivo"));
    }

    // ─── cambiarEstado ────────────────────────────────────────────────────────

    @Test
    public void testCambiarEstadoAConfirmadaExitosamente() {
        Tutoria tutoria = buildTutoriaPendiente();
        Usuario usuarioDocente = buildUsuarioDocente();

        when(tutoriaRepo.findById("t1")).thenReturn(Optional.of(tutoria));
        when(usuarioRepo.findByCorreoInstitucional("docente1@uce.edu.ec")).thenReturn(Optional.of(usuarioDocente));
        when(tutoriaRepo.save(any(Tutoria.class))).thenAnswer(inv -> inv.getArgument(0));
        when(usuarioRepo.findEstudianteById("e1")).thenReturn(Optional.of(estudiante));
        when(usuarioRepo.findById("u4")).thenReturn(Optional.of(usuarioEstudiante));

        Tutoria resultado = tutoriaService.cambiarEstado("t1", "Confirmada", null, "docente1@uce.edu.ec");

        assertEquals(EstadoTutoria.CONFIRMADA, resultado.getEstado());
        verify(historialService, times(1)).registrar(
                eq("t1"), eq(EstadoTutoria.PENDIENTE.getValor()),
                eq(EstadoTutoria.CONFIRMADA.getValor()),
                anyString(), any(LocalDateTime.class), anyString());
        verify(notificationService, times(1)).enviarNotificacion(
                anyString(), anyString(), eq("cambio_estado"), anyString());
    }

    @Test
    public void testCambiarEstadoTutoriaNoEncontradaLanzaExcepcion() {
        when(tutoriaRepo.findById("no-existe")).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () ->
                tutoriaService.cambiarEstado("no-existe", "Confirmada", null, "docente1@uce.edu.ec"));
    }

    @Test
    public void testCambiarEstadoConTransicionInvalidaLanzaExcepcion() {
        Tutoria tutoria = buildTutoriaPendiente();
        Usuario usuarioDocente = buildUsuarioDocente();

        when(tutoriaRepo.findById("t1")).thenReturn(Optional.of(tutoria));
        when(usuarioRepo.findByCorreoInstitucional("docente1@uce.edu.ec")).thenReturn(Optional.of(usuarioDocente));

        // PENDIENTE no puede pasar directo a ASISTIDA sin pasar por CONFIRMADA
        assertThrows(DomainException.class, () ->
                tutoriaService.cambiarEstado("t1", "Asistida", "Obs", "docente1@uce.edu.ec"));
    }

    @Test
    public void testCambiarEstadoACanceladaLiberaDisponibilidad() {
        Tutoria tutoria = buildTutoriaPendiente();
        Usuario usuarioDocente = buildUsuarioDocente();

        when(tutoriaRepo.findById("t1")).thenReturn(Optional.of(tutoria));
        when(usuarioRepo.findByCorreoInstitucional("docente1@uce.edu.ec")).thenReturn(Optional.of(usuarioDocente));
        when(disponibilidadRepo.findById("disp1")).thenReturn(Optional.of(disponibilidad));
        when(tutoriaRepo.save(any(Tutoria.class))).thenAnswer(inv -> inv.getArgument(0));
        when(usuarioRepo.findEstudianteById("e1")).thenReturn(Optional.of(estudiante));
        when(usuarioRepo.findById("u4")).thenReturn(Optional.of(usuarioEstudiante));

        tutoriaService.cambiarEstado("t1", "Cancelada", "Motivo de cancelación", "docente1@uce.edu.ec");

        // La disponibilidad debe ser liberada y guardada
        verify(disponibilidadRepo, times(1)).save(argThat(d -> "Disponible".equals(d.getEstado())));
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private Tutoria buildTutoriaPendiente() {
        return new Tutoria(
                "t1", "e1", "Antony Coello",
                "d1", "Carlos Perez",
                "1", "Arquitectura de Software",
                "disp1",
                baseTime, baseTime.plusHours(1),
                new MotivoTutoria("Consulta de examen"),
                EstadoTutoria.PENDIENTE,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusDays(1)
        );
    }

    private Usuario buildUsuarioDocente() {
        return new Usuario("u2", "Carlos", "Perez",
                new CorreoInstitucional("docente1@uce.edu.ec"), "docente123",
                RolUsuario.DOCENTE, EstadoUsuario.ACTIVO, LocalDateTime.now().minusDays(10));
    }
}
