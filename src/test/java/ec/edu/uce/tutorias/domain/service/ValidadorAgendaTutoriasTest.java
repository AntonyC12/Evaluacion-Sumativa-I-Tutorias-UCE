package ec.edu.uce.tutorias.domain.service;

import ec.edu.uce.tutorias.domain.exception.DomainException;
import ec.edu.uce.tutorias.domain.model.Tutoria;
import ec.edu.uce.tutorias.domain.repository.TutoriaRepository;
import ec.edu.uce.tutorias.domain.vo.EstadoTutoria;
import ec.edu.uce.tutorias.domain.vo.MotivoTutoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ValidadorAgendaTutoriasTest {

    private TutoriaRepository tutoriaRepository;
    private ValidadorAgendaTutorias validador;

    @BeforeEach
    void setUp() {
        tutoriaRepository = mock(TutoriaRepository.class);
        validador = new ValidadorAgendaTutorias(tutoriaRepository);
    }

    @Test
    void debePermitirAgendarSiNoHaySolapamientosNiDuplicados() {
        when(tutoriaRepository.buscarPorEstudianteYDocenteYMateriaEnRango(any(), any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(tutoriaRepository.buscarSolapadasParaEstudiante(any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(tutoriaRepository.buscarSolapadasParaDocente(any(), any(), any()))
                .thenReturn(Collections.emptyList());

        Tutoria nuevaTutoria = new Tutoria(
                "id1", "est1", "doc1", "mat1", "disp1",
                LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(1),
                new MotivoTutoria("Consulta")
        );

        assertDoesNotThrow(() -> validador.validarAgendamiento(nuevaTutoria));
    }

    @Test
    void noDebePermitirAgendarSiYaExisteTutoriaMismaSemana() {
        Tutoria tutoriaExistente = new Tutoria(
                "id2", "est1", "doc1", "mat1", "disp2",
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1),
                new MotivoTutoria("Consulta")
        );

        when(tutoriaRepository.buscarPorEstudianteYDocenteYMateriaEnRango(any(), any(), any(), any(), any()))
                .thenReturn(List.of(tutoriaExistente));

        Tutoria nuevaTutoria = new Tutoria(
                "id1", "est1", "doc1", "mat1", "disp1",
                LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(1),
                new MotivoTutoria("Consulta")
        );

        assertThrows(DomainException.class, () -> validador.validarAgendamiento(nuevaTutoria));
    }

    @Test
    void noDebePermitirAgendarSiEstudianteTieneSolapamiento() {
        Tutoria tutoriaExistente = new Tutoria(
                "id2", "est1", "doc2", "mat2", "disp2",
                LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(1),
                new MotivoTutoria("Otra tutoría")
        );

        when(tutoriaRepository.buscarPorEstudianteYDocenteYMateriaEnRango(any(), any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(tutoriaRepository.buscarSolapadasParaEstudiante(any(), any(), any()))
                .thenReturn(List.of(tutoriaExistente));

        Tutoria nuevaTutoria = new Tutoria(
                "id1", "est1", "doc1", "mat1", "disp1",
                LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(1),
                new MotivoTutoria("Consulta")
        );

        assertThrows(DomainException.class, () -> validador.validarAgendamiento(nuevaTutoria));
    }

    @Test
    void noDebePermitirAgendarSiDocenteTieneSolapamiento() {
        Tutoria tutoriaExistente = new Tutoria(
                "id2", "est2", "doc1", "mat2", "disp2",
                LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(1),
                new MotivoTutoria("Tutoría con otro estudiante")
        );

        when(tutoriaRepository.buscarPorEstudianteYDocenteYMateriaEnRango(any(), any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(tutoriaRepository.buscarSolapadasParaEstudiante(any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(tutoriaRepository.buscarSolapadasParaDocente(any(), any(), any()))
                .thenReturn(List.of(tutoriaExistente));

        Tutoria nuevaTutoria = new Tutoria(
                "id1", "est1", "doc1", "mat1", "disp1",
                LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(1),
                new MotivoTutoria("Consulta")
        );

        assertThrows(DomainException.class, () -> validador.validarAgendamiento(nuevaTutoria));
    }
}
