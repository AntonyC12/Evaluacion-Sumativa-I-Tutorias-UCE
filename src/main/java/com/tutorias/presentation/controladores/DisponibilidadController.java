package com.tutorias.presentation.controladores;

import com.tutorias.application.servicios.DisponibilidadApplicationService;
import com.tutorias.application.servicios.UsuarioApplicationService;
import com.tutorias.domain.entities.Disponibilidad;
import com.tutorias.domain.entities.Usuario;
import com.tutorias.domain.valueobjects.RangoHorario;
import com.tutorias.presentation.dto.DisponibilidadRequest;
import com.tutorias.presentation.dto.DisponibilidadResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/disponibilidades")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Disponibilidades", description = "Gestión de horarios de disponibilidad por docentes")
public class DisponibilidadController {

    private final DisponibilidadApplicationService disponibilidadApplicationService;
    private final UsuarioApplicationService usuarioApplicationService;

    @PostMapping
    @Operation(summary = "Registrar un bloque de disponibilidad (Docentes)")
    public DisponibilidadResponse registrar(
            @Valid @RequestBody DisponibilidadRequest request,
            @RequestHeader("X-Docente-Id") String docenteId) {
        Disponibilidad disp = disponibilidadApplicationService.registrarDisponibilidad(
                docenteId,
                request.getFecha(),
                new RangoHorario(request.getHoraInicio(), request.getHoraFin())
        );
        return mapToResponse(disp);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar un bloque de disponibilidad (siempre y cuando no esté reservado)")
    public DisponibilidadResponse editar(
            @PathVariable String id,
            @Valid @RequestBody DisponibilidadRequest request) {
        Disponibilidad disp = disponibilidadApplicationService.editarDisponibilidad(
                id,
                request.getFecha(),
                new RangoHorario(request.getHoraInicio(), request.getHoraFin())
        );
        return mapToResponse(disp);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un bloque de disponibilidad (siempre y cuando no esté reservado)")
    public void eliminar(@PathVariable String id) {
        disponibilidadApplicationService.eliminarDisponibilidad(id);
    }

    @GetMapping
    @Operation(summary = "Listar todas las disponibilidades. Opcionalmente filtrar por docenteId")
    public List<DisponibilidadResponse> listar(@RequestParam(required = false) String docenteId) {
        List<Disponibilidad> lista;
        if (docenteId != null && !docenteId.trim().isEmpty()) {
            lista = disponibilidadApplicationService.listarPorDocente(docenteId);
        } else {
            lista = disponibilidadApplicationService.listarTodas();
        }
        return lista.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private DisponibilidadResponse mapToResponse(Disponibilidad disp) {
        String docenteNombre = "Docente Desconocido";
        try {
            Usuario user = usuarioApplicationService.buscarPorId(disp.getDocenteId());
            docenteNombre = user.getNombre();
        } catch (Exception ignored) {}

        return new DisponibilidadResponse(
                disp.getId(),
                disp.getDocenteId(),
                docenteNombre,
                disp.getFecha(),
                disp.getRangoHorario().horaInicio(),
                disp.getRangoHorario().horaFin(),
                disp.isReservada()
        );
    }
}
