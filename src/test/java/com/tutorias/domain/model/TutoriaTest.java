package com.tutorias.domain.model;

import com.tutorias.domain.exception.DomainException;
import com.tutorias.domain.enums.EstadoTutoria;
import com.tutorias.domain.valueobject.MotivoTutoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TutoriaTest {

    private Tutoria tutoria;
    private LocalDateTime baseTime;

    @BeforeEach
    public void setUp() {
        baseTime = LocalDateTime.of(2026, 6, 10, 10, 0);
        tutoria = new Tutoria(
                "t-test-1",
                "e1",
                "Antony Coello",
                "d1",
                "Carlos Perez",
                "1",
                "Arquitectura de Software",
                "disp1",
                baseTime, // Inicio: 10 de junio a las 10:00
                baseTime.plusHours(1), // Fin: 10 de junio a las 11:00
                new MotivoTutoria("Dudas sobre patrones arquitectónicos"),
                EstadoTutoria.PENDIENTE,
                baseTime.minusDays(1),
                baseTime.minusDays(1)
        );
    }

    @Test
    public void testConfirmarTutoriaExitosamente() {
        tutoria.confirmar(baseTime.minusHours(5));
        assertEquals(EstadoTutoria.CONFIRMADA, tutoria.getEstado());
    }

    @Test
    public void testConfirmarTutoriaEstadoInvalido() {
        tutoria.confirmar(baseTime.minusHours(5)); // Pasa a CONFIRMADA
        assertThrows(DomainException.class, () -> tutoria.confirmar(baseTime.minusHours(4)));
    }

    @Test
    public void testRegistrarAsistenciaExitosamente() {
        tutoria.confirmar(baseTime.minusHours(5)); // CONFIRMADA
        tutoria.registrarAsistencia("Asistió puntualmente", baseTime.plusMinutes(30));
        assertEquals(EstadoTutoria.ASISTIDA, tutoria.getEstado());
        assertEquals("Asistió puntualmente", tutoria.getObservacion());
    }

    @Test
    public void testRegistrarAsistenciaSinConfirmar() {
        // PENDIENTE
        assertThrows(DomainException.class, () -> tutoria.registrarAsistencia("Intento", baseTime));
    }

    @Test
    public void testRegistrarAsistenciaObservacionObligatoria() {
        tutoria.confirmar(baseTime.minusHours(5));
        assertThrows(DomainException.class, () -> tutoria.registrarAsistencia("", baseTime));
        assertThrows(DomainException.class, () -> tutoria.registrarAsistencia(null, baseTime));
    }

    @Test
    public void testCancelacionEstudianteValida() {
        // Solicitud hecha 25 horas antes del inicio
        LocalDateTime ahora = baseTime.minusHours(25);
        tutoria.cancelar("estudiante", ahora);
        assertEquals(EstadoTutoria.CANCELADA, tutoria.getEstado());
    }

    @Test
    public void testCancelacionEstudianteInvalidaMenosDe24Horas() {
        // Solicitud hecha 23 horas y 59 minutos antes del inicio
        LocalDateTime ahora = baseTime.minusHours(23).minusMinutes(59);
        assertThrows(DomainException.class, () -> tutoria.cancelar("estudiante", ahora));
    }

    @Test
    public void testCancelacionDocenteEnCualquierMomento() {
        // Docente cancela faltando 2 horas para el inicio
        LocalDateTime ahora = baseTime.minusHours(2);
        tutoria.cancelar("docente", ahora);
        assertEquals(EstadoTutoria.CANCELADA, tutoria.getEstado());
    }

    @Test
    public void testCancelacionDesdeEstadoTerminalInvalida() {
        LocalDateTime ahora = baseTime.minusHours(30);
        tutoria.confirmar(ahora); // Pasa a CONFIRMADA
        tutoria.registrarAsistencia("Si asistió", baseTime.plusMinutes(10)); // terminal: ASISTIDA
        
        assertThrows(DomainException.class, () -> tutoria.cancelar("estudiante", baseTime.plusHours(1)));
        assertThrows(DomainException.class, () -> tutoria.cancelar("docente", baseTime.plusHours(1)));
    }
}
