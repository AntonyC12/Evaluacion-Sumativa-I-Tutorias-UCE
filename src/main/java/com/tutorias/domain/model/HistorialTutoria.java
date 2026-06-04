package com.tutorias.domain.model;

import java.time.LocalDateTime;

public class HistorialTutoria {
    private String id;
    private String tutoriaId;
    private String estadoAnterior;
    private String estadoNuevo;
    private String descripcion;
    private LocalDateTime fechaEvento;
    private String usuarioResponsable;

    public HistorialTutoria() {}

    public HistorialTutoria(String id, String tutoriaId, String estadoAnterior, String estadoNuevo,
                            String descripcion, LocalDateTime fechaEvento, String usuarioResponsable) {
        this.id = id;
        this.tutoriaId = tutoriaId;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.descripcion = descripcion;
        this.fechaEvento = fechaEvento;
        this.usuarioResponsable = usuarioResponsable;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTutoriaId() { return tutoriaId; }
    public void setTutoriaId(String tutoriaId) { this.tutoriaId = tutoriaId; }

    public String getEstadoAnterior() { return estadoAnterior; }
    public void setEstadoAnterior(String estadoAnterior) { this.estadoAnterior = estadoAnterior; }

    public String getEstadoNuevo() { return estadoNuevo; }
    public void setEstadoNuevo(String estadoNuevo) { this.estadoNuevo = estadoNuevo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getFechaEvento() { return fechaEvento; }
    public void setFechaEvento(LocalDateTime fechaEvento) { this.fechaEvento = fechaEvento; }

    public String getUsuarioResponsable() { return usuarioResponsable; }
    public void setUsuarioResponsable(String usuarioResponsable) { this.usuarioResponsable = usuarioResponsable; }
}
