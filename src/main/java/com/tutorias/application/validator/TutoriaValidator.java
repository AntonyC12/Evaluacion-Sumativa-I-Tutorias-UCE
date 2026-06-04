package com.tutorias.application.validator;

import com.tutorias.domain.model.Disponibilidad;
import com.tutorias.domain.model.Tutoria;
import com.tutorias.domain.exception.DomainException;
import com.tutorias.infrastructure.repository.TutoriaRepository;
import com.tutorias.domain.enums.EstadoTutoria;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Arrays;
import java.util.List;

@Component
public class TutoriaValidator {
    private final TutoriaRepository tutoriaRepo;

    public TutoriaValidator(TutoriaRepository tutoriaRepo) {
        this.tutoriaRepo = tutoriaRepo;
    }

    public void validarDuplicidadSemanal(String estudianteId, String docenteId, String materiaId, LocalDate fecha) {
        WeekFields weekFields = WeekFields.ISO;
        int semana = fecha.get(weekFields.weekOfWeekBasedYear());
        int anio = fecha.getYear();

        List<Tutoria> existentes = tutoriaRepo.findByEstudianteIdAndDocenteIdAndMateriaIdAndEstadoNot(
                estudianteId, docenteId, materiaId, EstadoTutoria.CANCELADA.getValor());

        for (Tutoria t : existentes) {
            LocalDate tFecha = t.getFechaHoraInicio().toLocalDate();
            int tSemana = tFecha.get(weekFields.weekOfWeekBasedYear());
            int tAnio = tFecha.getYear();

            if (semana == tSemana && anio == tAnio) {
                throw new DomainException("Ya has agendado una tutoría con este docente para esta materia esta misma semana");
            }
        }
    }

    public void validarSolapamientoEstudiante(String estudianteId, LocalDateTime inicio, LocalDateTime fin) {
        List<Tutoria> activas = tutoriaRepo.findByEstudianteIdAndEstadoIn(
                estudianteId, Arrays.asList(EstadoTutoria.PENDIENTE.getValor(), EstadoTutoria.CONFIRMADA.getValor()));

        for (Tutoria t : activas) {
            if (inicio.isBefore(t.getFechaHoraFin()) && fin.isAfter(t.getFechaHoraInicio())) {
                throw new DomainException("No se permite agendar dos tutorías del mismo estudiante que se crucen en horario");
            }
        }
    }
}
