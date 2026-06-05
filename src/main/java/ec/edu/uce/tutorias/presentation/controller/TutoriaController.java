package ec.edu.uce.tutorias.presentation.controller;

import ec.edu.uce.tutorias.application.dto.AgendarTutoriaRequest;
import ec.edu.uce.tutorias.application.dto.TutoriaResponse;
import ec.edu.uce.tutorias.application.usecase.*;
import ec.edu.uce.tutorias.domain.model.Tutoria;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tutorias")
public class TutoriaController {

    private final AgendarTutoriaUseCase agendarTutoriaUseCase;
    private final CancelarTutoriaUseCase cancelarTutoriaUseCase;
    private final ConfirmarTutoriaUseCase confirmarTutoriaUseCase;
    private final RegistrarAsistenciaUseCase registrarAsistenciaUseCase;
    private final RegistrarInasistenciaUseCase registrarInasistenciaUseCase;
    private final ListarTutoriasUseCase listarTutoriasUseCase;

    public TutoriaController(AgendarTutoriaUseCase agendarTutoriaUseCase,
                             CancelarTutoriaUseCase cancelarTutoriaUseCase,
                             ConfirmarTutoriaUseCase confirmarTutoriaUseCase,
                             RegistrarAsistenciaUseCase registrarAsistenciaUseCase,
                             RegistrarInasistenciaUseCase registrarInasistenciaUseCase,
                             ListarTutoriasUseCase listarTutoriasUseCase) {
        this.agendarTutoriaUseCase = agendarTutoriaUseCase;
        this.cancelarTutoriaUseCase = cancelarTutoriaUseCase;
        this.confirmarTutoriaUseCase = confirmarTutoriaUseCase;
        this.registrarAsistenciaUseCase = registrarAsistenciaUseCase;
        this.registrarInasistenciaUseCase = registrarInasistenciaUseCase;
        this.listarTutoriasUseCase = listarTutoriasUseCase;
    }

    @PostMapping
    public ResponseEntity<String> agendarTutoria(@RequestBody AgendarTutoriaRequest request) {
        String idTutoria = agendarTutoriaUseCase.ejecutar(request);
        return ResponseEntity.ok(idTutoria);
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarTutoria(@PathVariable String id, @RequestParam String estudianteId) {
        cancelarTutoriaUseCase.ejecutar(id, estudianteId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/confirmar")
    public ResponseEntity<Void> confirmarTutoria(@PathVariable String id, @RequestParam String docenteId) {
        confirmarTutoriaUseCase.ejecutar(id, docenteId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/asistencia")
    public ResponseEntity<Void> registrarAsistencia(@PathVariable String id, @RequestParam String docenteId) {
        registrarAsistenciaUseCase.ejecutar(id, docenteId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/inasistencia")
    public ResponseEntity<Void> registrarInasistencia(@PathVariable String id, @RequestParam String docenteId) {
        registrarInasistenciaUseCase.ejecutar(id, docenteId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<TutoriaResponse>> listarTutorias() {
        List<TutoriaResponse> responses = listarTutoriasUseCase.ejecutar().stream()
                .map(t -> new TutoriaResponse(
                        t.getId(),
                        t.getEstudianteId(),
                        t.getDocenteId(),
                        t.getMateriaId(),
                        t.getDisponibilidadId(),
                        t.getFechaHoraInicio().toString(),
                        t.getFechaHoraFin().toString(),
                        t.getMotivo().getValor(),
                        t.getEstado().name()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}
