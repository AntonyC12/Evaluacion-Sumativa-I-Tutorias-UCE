package com.tutorias.application.servicios;

import com.tutorias.domain.entities.Disponibilidad;
import com.tutorias.domain.entities.Usuario;
import com.tutorias.domain.exceptions.DomainException;
import com.tutorias.domain.repositorios.DisponibilidadRepository;
import com.tutorias.domain.repositorios.UsuarioRepository;
import com.tutorias.domain.services.ValidadorAgendaTutorias;
import com.tutorias.domain.valueobjects.RangoHorario;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DisponibilidadApplicationService {
    private final DisponibilidadRepository disponibilidadRepository;
    private final UsuarioRepository usuarioRepository;
    private final ValidadorAgendaTutorias validador = new ValidadorAgendaTutorias();

    public Disponibilidad registrarDisponibilidad(String docenteId, LocalDate fecha, RangoHorario rango) {
        validarDocenteActivo(docenteId);

        List<Disponibilidad> existentes = disponibilidadRepository.findByDocenteId(docenteId);
        validador.validarNoSolapamientoDisponibilidades(docenteId, fecha, rango, existentes, null);

        Disponibilidad disponibilidad = new Disponibilidad(null, docenteId, fecha, rango, false);
        return disponibilidadRepository.save(disponibilidad);
    }

    public Disponibilidad editarDisponibilidad(String disponibilidadId, LocalDate fecha, RangoHorario rango) {
        Disponibilidad disp = disponibilidadRepository.findById(disponibilidadId)
                .orElseThrow(() -> new DomainException("Disponibilidad no encontrada."));

        disp.validarParaEdicion();
        validarDocenteActivo(disp.getDocenteId());

        List<Disponibilidad> existentes = disponibilidadRepository.findByDocenteId(disp.getDocenteId());
        validador.validarNoSolapamientoDisponibilidades(disp.getDocenteId(), fecha, rango, existentes, disp.getId());

        disp.setFecha(fecha);
        disp.setRangoHorario(rango);
        return disponibilidadRepository.save(disp);
    }

    public void eliminarDisponibilidad(String disponibilidadId) {
        Disponibilidad disp = disponibilidadRepository.findById(disponibilidadId)
                .orElseThrow(() -> new DomainException("Disponibilidad no encontrada."));

        disp.validarParaEliminacion();
        disponibilidadRepository.delete(disp.getId());
    }

    public List<Disponibilidad> listarPorDocente(String docenteId) {
        return disponibilidadRepository.findByDocenteId(docenteId);
    }

    public List<Disponibilidad> listarTodas() {
        return disponibilidadRepository.findAll();
    }

    private void validarDocenteActivo(String docenteId) {
        Usuario usuario = usuarioRepository.findById(docenteId)
                .orElseThrow(() -> new DomainException("Docente no encontrado."));
        if (!usuario.isActivo()) {
            throw new DomainException("El docente se encuentra inactivo y no puede operar en el sistema.");
        }
        if (usuario.getRol() != com.tutorias.domain.enums.RolUsuario.DOCENTE) {
            throw new DomainException("El usuario debe tener rol DOCENTE.");
        }
    }
}
