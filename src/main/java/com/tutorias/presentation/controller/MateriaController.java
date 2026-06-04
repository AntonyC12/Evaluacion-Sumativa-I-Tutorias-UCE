package com.tutorias.presentation.controller;

import com.tutorias.domain.model.Materia;
import com.tutorias.infrastructure.repository.MateriaRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/materias")
@CrossOrigin(origins = "*")
public class MateriaController {
    private final MateriaRepository materiaRepo;

    public MateriaController(MateriaRepository materiaRepo) {
        this.materiaRepo = materiaRepo;
    }

    @GetMapping
    public List<Materia> listarMaterias() {
        return materiaRepo.findAll();
    }
}
