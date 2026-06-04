package com.tutorias.infrastructure.repository.mongo.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notificaciones")
public class NotificacionDocument {
    @Id
    private String id;
    private String tutoriaId;
    private String destinatario; // correo
    private String tipo; // creacion, cambio_estado, cancelacion, etc.
    private String mensaje;
    private String estadoEnvio; // Enviado
    private String fechaEnvio; // yyyy-MM-dd HH:mm:ss

    public NotificacionDocument() {}

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTutoriaId() { return tutoriaId; }
    public void setTutoriaId(String tutoriaId) { this.tutoriaId = tutoriaId; }

    public String getDestinatario() { return destinatario; }
    public void setDestinatario(String destinatario) { this.destinatario = destinatario; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public String getEstadoEnvio() { return estadoEnvio; }
    public void setEstadoEnvio(String estadoEnvio) { this.estadoEnvio = estadoEnvio; }

    public String getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(String fechaEnvio) { this.fechaEnvio = fechaEnvio; }
}
