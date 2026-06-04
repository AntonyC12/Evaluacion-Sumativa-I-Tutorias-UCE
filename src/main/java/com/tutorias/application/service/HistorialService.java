package com.tutorias.application.service;

import com.tutorias.domain.model.HistorialTutoria;
import com.tutorias.infrastructure.repository.HistorialRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class HistorialService {
    private final HistorialRepository historialRepo;

    public HistorialService(HistorialRepository historialRepo) {
        this.historialRepo = historialRepo;
    }

    public List<HistorialTutoria> obtenerHistorialPorTutoriaId(String tutoriaId) {
        return historialRepo.findByTutoriaId(tutoriaId);
    }

    public void registrar(String tutoriaId, String estadoAnterior, String estadoNuevo, 
                          String descripcion, LocalDateTime fecha, String usuarioResponsable) {
        HistorialTutoria historial = new HistorialTutoria(
                UUID.randomUUID().toString(),
                tutoriaId,
                estadoAnterior,
                estadoNuevo,
                descripcion,
                fecha,
                usuarioResponsable
        );
        historialRepo.save(historial);
    }
}
