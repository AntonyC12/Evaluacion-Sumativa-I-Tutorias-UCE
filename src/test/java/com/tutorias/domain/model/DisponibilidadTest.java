package com.tutorias.domain.model;

import com.tutorias.domain.exception.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para el modelo de dominio Disponibilidad.
 * Valida las reglas de negocio encapsuladas en la entidad
 * sin dependencias de infraestructura.
 */
public class DisponibilidadTest {

    private LocalDate fechaFutura;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    @BeforeEach
    public void setUp() {
        fechaFutura = LocalDate.of(2026, 7, 15);
        horaInicio = LocalTime.of(9, 0);
        horaFin = LocalTime.of(10, 0);
    }

    // ─── Creación y validación ────────────────────────────────────────────────

    @Test
    public void testCrearDisponibilidadValida() {
        Disponibilidad d = new Disponibilidad(
                "d-test-1", "docente1", "Carlos Perez",
                "mat-1", "Arquitectura de Software",
                fechaFutura, horaInicio, horaFin, "Disponible");

        assertNotNull(d);
        assertEquals("Disponible", d.getEstado());
        assertEquals(horaInicio, d.getHoraInicio());
        assertEquals(horaFin, d.getHoraFin());
    }

    @Test
    public void testCrearDisponibilidadSinDocenteIdLanzaExcepcion() {
        assertThrows(DomainException.class, () ->
                new Disponibilidad("d-test-2", null, "Carlos Perez",
                        "mat-1", "Arquitectura de Software",
                        fechaFutura, horaInicio, horaFin, "Disponible")
        );
    }

    @Test
    public void testCrearDisponibilidadSinMateriaIdLanzaExcepcion() {
        assertThrows(DomainException.class, () ->
                new Disponibilidad("d-test-3", "docente1", "Carlos Perez",
                        null, "Arquitectura de Software",
                        fechaFutura, horaInicio, horaFin, "Disponible")
        );
    }

    @Test
    public void testCrearDisponibilidadSinFechaLanzaExcepcion() {
        assertThrows(DomainException.class, () ->
                new Disponibilidad("d-test-4", "docente1", "Carlos Perez",
                        "mat-1", "Arquitectura de Software",
                        null, horaInicio, horaFin, "Disponible")
        );
    }

    @Test
    public void testCrearDisponibilidadHoraInicioIgualHoraFinLanzaExcepcion() {
        // inicio == fin → inválido
        assertThrows(DomainException.class, () ->
                new Disponibilidad("d-test-5", "docente1", "Carlos Perez",
                        "mat-1", "Arquitectura de Software",
                        fechaFutura, horaInicio, horaInicio, "Disponible")
        );
    }

    @Test
    public void testCrearDisponibilidadHoraInicioPosteriorHoraFinLanzaExcepcion() {
        // inicio > fin → inválido
        assertThrows(DomainException.class, () ->
                new Disponibilidad("d-test-6", "docente1", "Carlos Perez",
                        "mat-1", "Arquitectura de Software",
                        fechaFutura, horaFin, horaInicio, "Disponible")
        );
    }

    @Test
    public void testCrearDisponibilidadConEstadoInvalidoLanzaExcepcion() {
        assertThrows(DomainException.class, () ->
                new Disponibilidad("d-test-7", "docente1", "Carlos Perez",
                        "mat-1", "Arquitectura de Software",
                        fechaFutura, horaInicio, horaFin, "INVALIDO")
        );
    }

    // ─── Regla: reservar() ────────────────────────────────────────────────────

    @Test
    public void testReservarDisponibilidadDisponibleExitosamente() {
        Disponibilidad d = new Disponibilidad("d-test-8", "docente1", "Carlos Perez",
                "mat-1", "Arquitectura de Software", fechaFutura, horaInicio, horaFin, "Disponible");

        d.reservar();

        assertEquals("Reservado", d.getEstado());
    }

    @Test
    public void testReservarDisponibilidadYaReservadaLanzaExcepcion() {
        Disponibilidad d = new Disponibilidad("d-test-9", "docente1", "Carlos Perez",
                "mat-1", "Arquitectura de Software", fechaFutura, horaInicio, horaFin, "Reservado");

        assertThrows(DomainException.class, d::reservar);
    }

    // ─── Regla: liberar() ────────────────────────────────────────────────────

    @Test
    public void testLiberarDisponibilidadReservadaExitosamente() {
        Disponibilidad d = new Disponibilidad("d-test-10", "docente1", "Carlos Perez",
                "mat-1", "Arquitectura de Software", fechaFutura, horaInicio, horaFin, "Reservado");

        d.liberar();

        assertEquals("Disponible", d.getEstado());
    }

    // ─── Regla: verificarDisponibilidadEdicion() ─────────────────────────────

    @Test
    public void testVerificarEdicionDisponibleNoLanzaExcepcion() {
        Disponibilidad d = new Disponibilidad("d-test-11", "docente1", "Carlos Perez",
                "mat-1", "Arquitectura de Software", fechaFutura, horaInicio, horaFin, "Disponible");

        assertDoesNotThrow(d::verificarDisponibilidadEdicion);
    }

    @Test
    public void testVerificarEdicionReservadoLanzaExcepcion() {
        Disponibilidad d = new Disponibilidad("d-test-12", "docente1", "Carlos Perez",
                "mat-1", "Arquitectura de Software", fechaFutura, horaInicio, horaFin, "Reservado");

        assertThrows(DomainException.class, d::verificarDisponibilidadEdicion);
    }
}
