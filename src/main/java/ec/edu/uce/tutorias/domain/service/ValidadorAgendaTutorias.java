package ec.edu.uce.tutorias.domain.service;

import ec.edu.uce.tutorias.domain.exception.DomainException;
import ec.edu.uce.tutorias.domain.model.Tutoria;
import ec.edu.uce.tutorias.domain.repository.TutoriaRepository;
import ec.edu.uce.tutorias.domain.vo.EstadoTutoria;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

public class ValidadorAgendaTutorias {

    private final TutoriaRepository tutoriaRepository;

    public ValidadorAgendaTutorias(TutoriaRepository tutoriaRepository) {
        this.tutoriaRepository = tutoriaRepository;
    }

    public void validarAgendamiento(Tutoria nuevaTutoria) {
        LocalDateTime inicio = nuevaTutoria.getFechaHoraInicio();
        LocalDateTime fin = nuevaTutoria.getFechaHoraFin();

        // 1. Validar duplicidad en la misma semana para misma materia y docente
        LocalDateTime inicioSemana = inicio.with(DayOfWeek.MONDAY).withHour(0).withMinute(0);
        LocalDateTime finSemana = inicio.with(DayOfWeek.SUNDAY).withHour(23).withMinute(59);

        List<Tutoria> tutoriasMismaSemana = tutoriaRepository.buscarPorEstudianteYDocenteYMateriaEnRango(
                nuevaTutoria.getEstudianteId(),
                nuevaTutoria.getDocenteId(),
                nuevaTutoria.getMateriaId(),
                inicioSemana,
                finSemana
        );

        long activas = tutoriasMismaSemana.stream()
                .filter(t -> t.getEstado() != EstadoTutoria.CANCELADA)
                .count();

        if (activas > 0) {
            throw new DomainException("El estudiante ya tiene una tutoría activa para esta materia y docente en la misma semana");
        }

        // 2. Validar solapamiento de tutorías del estudiante
        List<Tutoria> tutoriasSolapadas = tutoriaRepository.buscarSolapadasParaEstudiante(
                nuevaTutoria.getEstudianteId(),
                inicio,
                fin
        );

        long solapadasActivas = tutoriasSolapadas.stream()
                .filter(t -> t.getEstado() != EstadoTutoria.CANCELADA)
                .count();

        if (solapadasActivas > 0) {
            throw new DomainException("El estudiante ya tiene una tutoría que se cruza en horario");
        }

        // 3. Validar solapamiento de tutorías del docente
        List<Tutoria> tutoriasSolapadasDocente = tutoriaRepository.buscarSolapadasParaDocente(
                nuevaTutoria.getDocenteId(),
                inicio,
                fin
        );

        long solapadasDocenteActivas = tutoriasSolapadasDocente.stream()
                .filter(t -> t.getEstado() != EstadoTutoria.CANCELADA)
                .count();

        if (solapadasDocenteActivas > 0) {
            throw new DomainException("El docente ya tiene una tutoría programada que se cruza en ese horario");
        }
    }
}
