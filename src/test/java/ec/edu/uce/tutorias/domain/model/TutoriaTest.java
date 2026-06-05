package ec.edu.uce.tutorias.domain.model;

import ec.edu.uce.tutorias.domain.exception.DomainException;
import ec.edu.uce.tutorias.domain.vo.EstadoTutoria;
import ec.edu.uce.tutorias.domain.vo.MotivoTutoria;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TutoriaTest {

    @Test
    void debeCrearseConEstadoPendiente() {
        Tutoria tutoria = new Tutoria(
                "id1", "est1", "doc1", "mat1", "disp1",
                LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(1),
                new MotivoTutoria("Consulta")
        );

        assertEquals(EstadoTutoria.PENDIENTE, tutoria.getEstado());
    }

    @Test
    void noDebePermitirCrearTutoriaEnElPasado() {
        assertThrows(DomainException.class, () -> {
            new Tutoria(
                    "id1", "est1", "doc1", "mat1", "disp1",
                    LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1).plusHours(1),
                    new MotivoTutoria("Consulta")
            );
        });
    }

    @Test
    void debeCancelarSiFaltanMasDe24Horas() {
        Tutoria tutoria = new Tutoria(
                "id1", "est1", "doc1", "mat1", "disp1",
                LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(1),
                new MotivoTutoria("Consulta")
        );

        tutoria.cancelar();

        assertEquals(EstadoTutoria.CANCELADA, tutoria.getEstado());
    }

    @Test
    void noDebeCancelarSiFaltaMenosDe24Horas() {
        Tutoria tutoria = new Tutoria(
                "id1", "est1", "doc1", "mat1", "disp1",
                LocalDateTime.now().plusHours(10), LocalDateTime.now().plusHours(11),
                new MotivoTutoria("Consulta")
        );

        assertThrows(DomainException.class, tutoria::cancelar);
    }

    @Test
    void debeConfirmarSoloSiEstaPendiente() {
        Tutoria tutoria = new Tutoria(
                "id1", "est1", "doc1", "mat1", "disp1",
                LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(1),
                new MotivoTutoria("Consulta")
        );

        tutoria.confirmar();

        assertEquals(EstadoTutoria.CONFIRMADA, tutoria.getEstado());
    }

    @Test
    void noDebeRegistrarAsistenciaSiNoEstaConfirmada() {
        Tutoria tutoria = new Tutoria(
                "id1", "est1", "doc1", "mat1", "disp1",
                LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(1),
                new MotivoTutoria("Consulta")
        );

        assertThrows(DomainException.class, tutoria::registrarAsistencia);
    }
}
