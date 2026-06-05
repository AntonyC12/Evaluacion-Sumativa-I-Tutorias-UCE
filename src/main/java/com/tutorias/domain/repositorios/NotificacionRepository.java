package com.tutorias.domain.repositorios;

import com.tutorias.domain.entities.Notificacion;
import java.util.List;

public interface NotificacionRepository {
    Notificacion save(Notificacion notificacion);
    List<Notificacion> findByUsuarioId(String usuarioId);
    List<Notificacion> findAll();
}
