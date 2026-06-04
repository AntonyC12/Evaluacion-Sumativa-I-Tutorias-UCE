package com.tutorias.application.validator;

import com.tutorias.domain.model.Disponibilidad;
import com.tutorias.domain.exception.DomainException;
import com.tutorias.infrastructure.repository.DisponibilidadRepository;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DisponibilidadValidator {
    private final DisponibilidadRepository disponibilidadRepo;

    public DisponibilidadValidator(DisponibilidadRepository disponibilidadRepo) {
        this.disponibilidadRepo = disponibilidadRepo;
    }

    public void validarNoSolapamientoDocente(String docenteId, LocalDate fecha, LocalTime inicio, LocalTime fin) {
        List<Disponibilidad> existentes = disponibilidadRepo.findByDocenteIdAndFechaAndEstado(docenteId, fecha.toString(), "Disponible");
        for (Disponibilidad ext : existentes) {
            if (inicio.isBefore(ext.getHoraFin()) && fin.isAfter(ext.getHoraInicio())) {
                throw new DomainException("El horario se solapa con una disponibilidad ya registrada para este docente");
            }
        }
    }

    public void validarFechaFutura(LocalDate fecha, LocalTime inicio, LocalDateTime ahora) {
        LocalDateTime fechaHoraDisp = LocalDateTime.of(fecha, inicio);
        if (fechaHoraDisp.isBefore(ahora)) {
            throw new DomainException("No se puede crear una disponibilidad en el pasado");
        }
    }
}
