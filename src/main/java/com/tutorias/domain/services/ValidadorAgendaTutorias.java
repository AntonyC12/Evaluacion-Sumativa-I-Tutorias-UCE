package com.tutorias.domain.services;

import com.tutorias.domain.entities.Disponibilidad;
import com.tutorias.domain.entities.Tutoria;
import com.tutorias.domain.enums.EstadoTutoria;
import com.tutorias.domain.exceptions.DomainException;
import com.tutorias.domain.valueobjects.RangoHorario;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.List;

public class ValidadorAgendaTutorias {

    public void validarNoDuplicidadSemanal(
            String estudianteId,
            String docenteId,
            String materiaId,
            LocalDate fecha,
            List<Tutoria> tutoriasExistentes) {

        for (Tutoria t : tutoriasExistentes) {
            if (t.getEstado() == EstadoTutoria.CANCELADA) {
                continue;
            }
            if (t.getEstudianteId().equals(estudianteId)
                    && t.getDocenteId().equals(docenteId)
                    && t.getMateriaId().equals(materiaId)
                    && sonMismaSemana(t.getFechaHora().fecha(), fecha)) {
                throw new DomainException("Un estudiante no puede tener más de una tutoría con el mismo docente y la misma materia en la misma semana.");
            }
        }
    }

    public void validarNoCruceEstudiante(
            String estudianteId,
            LocalDate fecha,
            RangoHorario rango,
            List<Tutoria> tutoriasExistentes) {

        for (Tutoria t : tutoriasExistentes) {
            if (t.getEstado() == EstadoTutoria.CANCELADA) {
                continue;
            }
            if (t.getEstudianteId().equals(estudianteId)
                    && t.getFechaHora().fecha().equals(fecha)
                    && t.getFechaHora().rangoHorario().seSolapaCon(rango)) {
                throw new DomainException("El estudiante ya tiene otra tutoría que se cruza en horario en la fecha seleccionada.");
            }
        }
    }

    public void validarNoCruceDocente(
            String docenteId,
            LocalDate fecha,
            RangoHorario rango,
            List<Tutoria> tutoriasExistentes) {

        for (Tutoria t : tutoriasExistentes) {
            if (t.getEstado() == EstadoTutoria.CANCELADA) {
                continue;
            }
            if (t.getDocenteId().equals(docenteId)
                    && t.getFechaHora().fecha().equals(fecha)
                    && t.getFechaHora().rangoHorario().seSolapaCon(rango)) {
                throw new DomainException("El docente ya tiene otra tutoría que se cruza en horario en la fecha seleccionada.");
            }
        }
    }

    public void validarNoSolapamientoDisponibilidades(
            String docenteId,
            LocalDate fecha,
            RangoHorario rango,
            List<Disponibilidad> disponibilidadesExistentes,
            String excluirDisponibilidadId) {

        for (Disponibilidad d : disponibilidadesExistentes) {
            if (excluirDisponibilidadId != null && d.getId() != null && d.getId().equals(excluirDisponibilidadId)) {
                continue;
            }
            if (d.getDocenteId().equals(docenteId)
                    && d.getFecha().equals(fecha)
                    && d.getRangoHorario().seSolapaCon(rango)) {
                throw new DomainException("No puede existir solapamiento entre disponibilidades del mismo docente.");
            }
        }
    }

    private boolean sonMismaSemana(LocalDate d1, LocalDate d2) {
        TemporalField woy = WeekFields.ISO.weekOfWeekBasedYear();
        TemporalField wob = WeekFields.ISO.weekBasedYear();
        return d1.get(woy) == d2.get(woy) && d1.get(wob) == d2.get(wob);
    }
}
