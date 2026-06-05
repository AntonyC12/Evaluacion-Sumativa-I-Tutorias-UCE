package ec.edu.uce.tutorias.application.usecase;

import ec.edu.uce.tutorias.domain.model.Disponibilidad;
import ec.edu.uce.tutorias.domain.repository.DisponibilidadRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarDisponibilidadesUseCase {

    private final DisponibilidadRepository disponibilidadRepository;

    public ListarDisponibilidadesUseCase(DisponibilidadRepository disponibilidadRepository) {
        this.disponibilidadRepository = disponibilidadRepository;
    }

    public List<Disponibilidad> ejecutar() {
        return disponibilidadRepository.buscarTodas();
    }
}
