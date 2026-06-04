package com.tutorias.presentation.controller;

import com.tutorias.application.service.TutoriaService;
import com.tutorias.domain.model.Tutoria;
import com.tutorias.domain.model.HistorialTutoria;
import com.tutorias.presentation.dto.request.CrearTutoriaRequest;
import com.tutorias.presentation.dto.request.ActualizarEstadoTutoriaRequest;
import com.tutorias.presentation.dto.response.TutoriaResponse;
import com.tutorias.presentation.mapper.TutoriaMapper;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tutorias")
@CrossOrigin(origins = "*")
public class TutoriaController {
    private final TutoriaService tutoriaService;

    public TutoriaController(TutoriaService tutoriaService) {
        this.tutoriaService = tutoriaService;
    }

    @GetMapping
    public List<TutoriaResponse> listar(
            @RequestParam(value = "estudianteId", required = false) String estudianteId,
            @RequestParam(value = "docenteId", required = false) String docenteId,
            @RequestParam(value = "estado", required = false) String estado,
            @RequestParam(value = "estudianteNombre", required = false) String estudianteNombre,
            @RequestParam(value = "docenteNombre", required = false) String docenteNombre) {
        return tutoriaService.listarTutorias(estudianteId, docenteId, estado, estudianteNombre, docenteNombre).stream()
                .map(TutoriaMapper::toResponse)
                .collect(Collectors.toList());
    }

    @PostMapping
    public TutoriaResponse agendar(@RequestBody CrearTutoriaRequest req) {
        Tutoria t = tutoriaService.agendarTutoria(
                req.estudianteId(),
                req.disponibilidadId(),
                req.motivo()
        );
        return TutoriaMapper.toResponse(t);
    }

    @PutMapping("/{id}/status")
    public TutoriaResponse cambiarEstado(
            @PathVariable("id") String id,
            @RequestBody ActualizarEstadoTutoriaRequest req) {
        Tutoria t = tutoriaService.cambiarEstado(
                id,
                req.nuevoEstado(),
                req.observacion(),
                req.usuarioResponsable()
        );
        return TutoriaMapper.toResponse(t);
    }

    @GetMapping("/{id}/historial")
    public List<HistorialTutoria> obtenerHistorial(@PathVariable("id") String id) {
        return tutoriaService.obtenerHistorial(id);
    }
}
