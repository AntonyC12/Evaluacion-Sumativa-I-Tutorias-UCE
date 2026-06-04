package com.tutorias.presentation.controller;

import com.tutorias.application.service.DisponibilidadService;
import com.tutorias.domain.model.Disponibilidad;
import com.tutorias.presentation.dto.request.CrearDisponibilidadRequest;
import com.tutorias.presentation.dto.response.DisponibilidadResponse;
import com.tutorias.presentation.mapper.DisponibilidadMapper;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/disponibilidades")
@CrossOrigin(origins = "*")
public class DisponibilidadController {
    private final DisponibilidadService disponibilidadService;

    public DisponibilidadController(DisponibilidadService disponibilidadService) {
        this.disponibilidadService = disponibilidadService;
    }

    @GetMapping
    public List<DisponibilidadResponse> listar(
            @RequestParam(value = "estado", required = false) String estado,
            @RequestParam(value = "docenteId", required = false) String docenteId,
            @RequestParam(value = "materiaId", required = false) String materiaId,
            @RequestParam(value = "fecha", required = false) String fecha) {
        return disponibilidadService.listarDisponibilidades(estado, docenteId, materiaId, fecha).stream()
                .map(DisponibilidadMapper::toResponse)
                .collect(Collectors.toList());
    }

    @PostMapping
    public DisponibilidadResponse publicar(@RequestBody CrearDisponibilidadRequest req) {
        Disponibilidad d = disponibilidadService.publicarDisponibilidad(
                req.docenteId(),
                req.materiaId(),
                req.fecha(),
                req.horaInicio(),
                req.horaFin()
        );
        return DisponibilidadMapper.toResponse(d);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable("id") String id) {
        disponibilidadService.eliminarDisponibilidad(id);
    }
}
