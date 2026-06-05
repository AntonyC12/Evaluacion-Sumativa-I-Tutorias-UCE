package com.tutorias.presentation.controladores;

import com.tutorias.domain.entities.Materia;
import com.tutorias.domain.repositorios.MateriaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/materias")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Materias", description = "Consulta de materias académicas")
public class MateriaController {

    private final MateriaRepository materiaRepository;

    @GetMapping
    @Operation(summary = "Listar todas las materias académicas registradas")
    public List<Materia> listar() {
        return materiaRepository.findAll();
    }
}
