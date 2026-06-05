package ec.edu.uce.tutorias.presentation.controller;

import ec.edu.uce.tutorias.application.dto.RegistrarDisponibilidadRequest;
import ec.edu.uce.tutorias.application.dto.DisponibilidadResponse;
import ec.edu.uce.tutorias.application.usecase.RegistrarDisponibilidadUseCase;
import ec.edu.uce.tutorias.application.usecase.ListarDisponibilidadesUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/disponibilidades")
public class DisponibilidadController {

    private final RegistrarDisponibilidadUseCase registrarDisponibilidadUseCase;
    private final ListarDisponibilidadesUseCase listarDisponibilidadesUseCase;

    public DisponibilidadController(RegistrarDisponibilidadUseCase registrarDisponibilidadUseCase,
                                    ListarDisponibilidadesUseCase listarDisponibilidadesUseCase) {
        this.registrarDisponibilidadUseCase = registrarDisponibilidadUseCase;
        this.listarDisponibilidadesUseCase = listarDisponibilidadesUseCase;
    }

    @PostMapping
    public ResponseEntity<String> registrarDisponibilidad(@RequestBody RegistrarDisponibilidadRequest request) {
        String idDisponibilidad = registrarDisponibilidadUseCase.ejecutar(request);
        return ResponseEntity.ok(idDisponibilidad);
    }

    @GetMapping
    public ResponseEntity<List<DisponibilidadResponse>> listarDisponibilidades() {
        List<DisponibilidadResponse> responses = listarDisponibilidadesUseCase.ejecutar().stream()
                .map(d -> new DisponibilidadResponse(
                        d.getId(),
                        d.getDocenteId(),
                        d.getMateriaId(),
                        d.getFecha().toString(),
                        d.getRangoHorario().getHoraInicio().toString(),
                        d.getRangoHorario().getHoraFin().toString(),
                        d.isReservado()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}
