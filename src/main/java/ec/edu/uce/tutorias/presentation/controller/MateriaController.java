package ec.edu.uce.tutorias.presentation.controller;

import ec.edu.uce.tutorias.application.dto.MateriaResponse;
import ec.edu.uce.tutorias.application.usecase.ListarMateriasUseCase;
import ec.edu.uce.tutorias.application.usecase.RegistrarMateriaUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/materias")
public class MateriaController {

    private final ListarMateriasUseCase listarMateriasUseCase;
    private final RegistrarMateriaUseCase registrarMateriaUseCase;

    public MateriaController(ListarMateriasUseCase listarMateriasUseCase,
                             RegistrarMateriaUseCase registrarMateriaUseCase) {
        this.listarMateriasUseCase = listarMateriasUseCase;
        this.registrarMateriaUseCase = registrarMateriaUseCase;
    }

    @GetMapping
    public ResponseEntity<List<MateriaResponse>> listarMaterias() {
        List<MateriaResponse> responses = listarMateriasUseCase.ejecutar().stream()
                .map(m -> new MateriaResponse(m.getId(), m.getNombre()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<String> registrarMateria(@RequestParam String nombre) {
        String id = registrarMateriaUseCase.ejecutar(null, nombre);
        return ResponseEntity.ok(id);
    }
}
