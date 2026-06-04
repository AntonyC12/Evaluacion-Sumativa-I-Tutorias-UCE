package com.tutorias.infrastructure.notification;

import com.tutorias.domain.model.Notificacion;
import com.tutorias.infrastructure.repository.mongo.MongoNotificacionRepository;
import com.tutorias.infrastructure.repository.mongo.mapper.PersistenciaMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private final MongoNotificacionRepository notificacionRepo;

    public NotificationService(MongoNotificacionRepository notificacionRepo) {
        this.notificacionRepo = notificacionRepo;
    }

    public List<Notificacion> listarNotificaciones(String destinatario) {
        return notificacionRepo.findByDestinatario(destinatario).stream()
                .map(PersistenciaMapper::toDomain)
                .collect(Collectors.toList());
    }

    public void enviarNotificacion(String tutoriaId, String destinatario, String tipo, String mensaje) {
        Notificacion notif = new Notificacion(
                UUID.randomUUID().toString(),
                tutoriaId,
                destinatario,
                tipo,
                mensaje,
                "Enviado",
                LocalDateTime.now()
        );
        notificacionRepo.save(PersistenciaMapper.toDocument(notif));
    }
}
