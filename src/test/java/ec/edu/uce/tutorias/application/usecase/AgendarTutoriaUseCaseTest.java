package ec.edu.uce.tutorias.application.usecase;

import ec.edu.uce.tutorias.application.dto.AgendarTutoriaRequest;
import ec.edu.uce.tutorias.domain.exception.DomainException;
import ec.edu.uce.tutorias.domain.model.Disponibilidad;
import ec.edu.uce.tutorias.domain.model.Materia;
import ec.edu.uce.tutorias.domain.model.Usuario;
import ec.edu.uce.tutorias.domain.repository.DisponibilidadRepository;
import ec.edu.uce.tutorias.domain.repository.MateriaRepository;
import ec.edu.uce.tutorias.domain.repository.TutoriaRepository;
import ec.edu.uce.tutorias.domain.repository.UsuarioRepository;
import ec.edu.uce.tutorias.domain.vo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AgendarTutoriaUseCaseTest {

    private TutoriaRepository tutoriaRepository;
    private DisponibilidadRepository disponibilidadRepository;
    private UsuarioRepository usuarioRepository;
    private MateriaRepository materiaRepository;
    private AgendarTutoriaUseCase useCase;

    @BeforeEach
    void setUp() {
        tutoriaRepository = mock(TutoriaRepository.class);
        disponibilidadRepository = mock(DisponibilidadRepository.class);
        usuarioRepository = mock(UsuarioRepository.class);
        materiaRepository = mock(MateriaRepository.class);

        useCase = new AgendarTutoriaUseCase(
                tutoriaRepository,
                disponibilidadRepository,
                usuarioRepository,
                materiaRepository
        );
    }

    @Test
    void debeAgendarTutoriaExitosamente() {
        Usuario estudiante = new Usuario("est1", new CorreoInstitucional("estudiante@uce.edu.ec"), Rol.ESTUDIANTE);
        Usuario docente = new Usuario("doc1", new CorreoInstitucional("docente@uce.edu.ec"), Rol.DOCENTE);
        Materia materia = new Materia("mat1", "Arquitectura");
        
        LocalDate mañana = LocalDate.now().plusDays(1);
        RangoHorario rango = new RangoHorario(LocalTime.of(9, 0), LocalTime.of(10, 0));
        Disponibilidad disponibilidad = new Disponibilidad("disp1", "doc1", "mat1", mañana, rango);

        when(usuarioRepository.buscarPorId("est1")).thenReturn(Optional.of(estudiante));
        when(usuarioRepository.buscarPorId("doc1")).thenReturn(Optional.of(docente));
        when(materiaRepository.buscarPorId("mat1")).thenReturn(Optional.of(materia));
        when(disponibilidadRepository.buscarPorId("disp1")).thenReturn(Optional.of(disponibilidad));

        // Mock del Domain Service validation calls
        when(tutoriaRepository.buscarPorEstudianteYDocenteYMateriaEnRango(any(), any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(tutoriaRepository.buscarSolapadasParaEstudiante(any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(tutoriaRepository.buscarSolapadasParaDocente(any(), any(), any()))
                .thenReturn(Collections.emptyList());

        AgendarTutoriaRequest request = new AgendarTutoriaRequest();
        request.setEstudianteId("est1");
        request.setDocenteId("doc1");
        request.setMateriaId("mat1");
        request.setDisponibilidadId("disp1");
        request.setFechaHoraInicio(LocalDateTime.of(mañana, LocalTime.of(9, 0)));
        request.setFechaHoraFin(LocalDateTime.of(mañana, LocalTime.of(10, 0)));
        request.setMotivo("Dudas");

        String idTutoria = useCase.ejecutar(request);

        assertNotNull(idTutoria);
        assertTrue(disponibilidad.isReservado());
        verify(disponibilidadRepository).guardar(disponibilidad);
        verify(tutoriaRepository).guardar(any());
    }

    @Test
    void debeRechazarSiEstudianteInactivo() {
        Usuario estudiante = new Usuario("est1", new CorreoInstitucional("estudiante@uce.edu.ec"), Rol.ESTUDIANTE);
        estudiante.desactivar(); // Inactivo

        when(usuarioRepository.buscarPorId("est1")).thenReturn(Optional.of(estudiante));

        AgendarTutoriaRequest request = new AgendarTutoriaRequest();
        request.setEstudianteId("est1");

        assertThrows(DomainException.class, () -> useCase.ejecutar(request));
    }

    @Test
    void debeRechazarSiDocenteInactivo() {
        Usuario estudiante = new Usuario("est1", new CorreoInstitucional("estudiante@uce.edu.ec"), Rol.ESTUDIANTE);
        Usuario docente = new Usuario("doc1", new CorreoInstitucional("docente@uce.edu.ec"), Rol.DOCENTE);
        docente.desactivar(); // Inactivo

        when(usuarioRepository.buscarPorId("est1")).thenReturn(Optional.of(estudiante));
        when(usuarioRepository.buscarPorId("doc1")).thenReturn(Optional.of(docente));

        AgendarTutoriaRequest request = new AgendarTutoriaRequest();
        request.setEstudianteId("est1");
        request.setDocenteId("doc1");

        assertThrows(DomainException.class, () -> useCase.ejecutar(request));
    }
}
