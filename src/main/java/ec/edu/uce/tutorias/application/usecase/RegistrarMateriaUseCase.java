package ec.edu.uce.tutorias.application.usecase;

import ec.edu.uce.tutorias.domain.model.Materia;
import ec.edu.uce.tutorias.domain.repository.MateriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RegistrarMateriaUseCase {

    private final MateriaRepository materiaRepository;

    public RegistrarMateriaUseCase(MateriaRepository materiaRepository) {
        this.materiaRepository = materiaRepository;
    }

    @Transactional
    public String ejecutar(String id, String nombre) {
        String finalId = (id == null || id.trim().isEmpty()) ? UUID.randomUUID().toString() : id;
        Materia materia = new Materia(finalId, nombre);
        materiaRepository.guardar(materia);
        return finalId;
    }
}
