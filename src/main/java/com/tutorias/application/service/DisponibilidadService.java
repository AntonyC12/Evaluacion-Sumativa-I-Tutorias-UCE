package com.tutorias.application.service;

import com.tutorias.domain.model.Disponibilidad;
import com.tutorias.domain.model.Docente;
import com.tutorias.domain.model.Materia;
import com.tutorias.domain.model.Usuario;
import com.tutorias.application.validator.DisponibilidadValidator;
import com.tutorias.infrastructure.repository.DisponibilidadRepository;
import com.tutorias.infrastructure.repository.MateriaRepository;
import com.tutorias.infrastructure.repository.UsuarioRepository;
import com.tutorias.domain.exception.DomainException;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DisponibilidadService {
    private final DisponibilidadRepository disponibilidadRepo;
    private final UsuarioRepository usuarioRepo;
    private final MateriaRepository materiaRepo;
    private final DisponibilidadValidator disponibilidadValidator;

    public DisponibilidadService(DisponibilidadRepository disponibilidadRepo,
                                  UsuarioRepository usuarioRepo,
                                  MateriaRepository materiaRepo,
                                  DisponibilidadValidator disponibilidadValidator) {
        this.disponibilidadRepo = disponibilidadRepo;
        this.usuarioRepo = usuarioRepo;
        this.materiaRepo = materiaRepo;
        this.disponibilidadValidator = disponibilidadValidator;
    }

    public List<Disponibilidad> listarDisponibilidades(String estado, String docenteId, String materiaId, String fecha) {
        return disponibilidadRepo.findByFiltros(estado, docenteId, materiaId, fecha);
    }

    public Disponibilidad publicarDisponibilidad(String docenteId, String materiaId, String fechaStr, 
                                                   String horaInicioStr, String horaFinStr) {
        LocalDate fecha = LocalDate.parse(fechaStr);
        LocalTime horaInicio = LocalTime.parse(horaInicioStr);
        LocalTime horaFin = LocalTime.parse(horaFinStr);

        LocalDateTime ahora = LocalDateTime.now();

        // 1. Validar fecha futura
        disponibilidadValidator.validarFechaFutura(fecha, horaInicio, ahora);

        // 2. Validar no solapamiento docente
        disponibilidadValidator.validarNoSolapamientoDocente(docenteId, fecha, horaInicio, horaFin);

        // Obtener nombres para denormalizar
        Docente docente = usuarioRepo.findDocenteById(docenteId)
                .orElseThrow(() -> new DomainException("El docente no existe"));
        Usuario u = usuarioRepo.findById(docente.getUsuarioId())
                .orElseThrow(() -> new DomainException("Usuario del docente no encontrado"));
        String docenteNombre = u.getNombre() + " " + u.getApellido();

        Materia materia = materiaRepo.findById(materiaId)
                .orElseThrow(() -> new DomainException("La materia no existe"));
        String materiaNombre = materia.getNombre();

        Disponibilidad disp = new Disponibilidad(
                UUID.randomUUID().toString(),
                docenteId,
                docenteNombre,
                materiaId,
                materiaNombre,
                fecha,
                horaInicio,
                horaFin,
                "Disponible"
        );

        return disponibilidadRepo.save(disp);
    }

    public void eliminarDisponibilidad(String id) {
        Disponibilidad disp = disponibilidadRepo.findById(id)
                .orElseThrow(() -> new DomainException("Disponibilidad no encontrada"));
        
        disp.verificarDisponibilidadEdicion();
        
        disponibilidadRepo.deleteById(id);
    }
}
