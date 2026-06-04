package com.tutorias.domain.model;

import java.time.LocalDateTime;

public class Notificacion {
    private String id;
    private String tutoriaId;
    private String destinatario;
    private String tipo;
    private String mensaje;
    private String estadoEnvio; // Enviado
    private LocalDateTime fechaEnvio;

    public Notificacion() {}

    public Notificacion(String id, String tutoriaId, String destinatario, String tipo, String mensaje,
                        String estadoEnvio, LocalDateTime fechaEnvio) {
        this.id = id;
        this.tutoriaId = tutoriaId;
        this.destinatario = destinatario;
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.estadoEnvio = estadoEnvio;
        this.fechaEnvio = fechaEnvio;
    }

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

    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }
}
